package com.picsy.service;

import com.picsy.domain.Agent;
import com.picsy.domain.CommunityState;
import com.picsy.domain.Transaction;
import com.picsy.domain.matrix.ContributionCalculator;
import com.picsy.domain.matrix.ContributionResult;
import com.picsy.domain.matrix.MatrixAdjustments;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class CommunityService {

    private final ContributionCalculator contributionCalculator;
    private final AtomicReference<CommunityState> stateRef;

    public CommunityService(CommunityState seedState, ContributionCalculator contributionCalculator) {
        this.contributionCalculator = contributionCalculator;
        ContributionResult initialResult = contributionCalculator.recalculate(seedState.getEvaluationMatrix());
        CommunityState recalculated = rebuildState(seedState, seedState.getEvaluationMatrix(), initialResult, seedState.getTransactions(), Instant.now());
        this.stateRef = new AtomicReference<>(recalculated);
    }

    public CommunityState snapshot() {
        return stateRef.get();
    }

    public CommunityState applyTransaction(UUID buyerId, UUID sellerId, double amount, String note) {
        Objects.requireNonNull(buyerId, "buyerId");
        Objects.requireNonNull(sellerId, "sellerId");
        if (buyerId.equals(sellerId)) {
            throw new IllegalArgumentException("Buyer and seller must be different");
        }
        return stateRef.updateAndGet(current -> {
            int buyerIndex = indexOrThrow(current, buyerId);
            int sellerIndex = indexOrThrow(current, sellerId);
            double[][] updatedMatrix = MatrixAdjustments.applyTransaction(current.getEvaluationMatrix(), buyerIndex, sellerIndex, amount);
            ContributionResult result = contributionCalculator.recalculate(updatedMatrix);
            List<Transaction> transactions = new ArrayList<>(current.getTransactions());
            transactions.add(Transaction.of(buyerId, sellerId, amount, note));
            return rebuildState(current, updatedMatrix, result, transactions, Instant.now());
        });
    }

    public CommunityState applyNaturalRecovery() {
        return stateRef.updateAndGet(current -> {
            double gamma = current.getRecoveryConfig().getGamma();
            double[][] adjustedMatrix = MatrixAdjustments.applyNaturalRecovery(current.getEvaluationMatrix(), gamma);
            ContributionResult result = contributionCalculator.recalculate(adjustedMatrix);
            return rebuildState(current, adjustedMatrix, result, current.getTransactions(), Instant.now());
        });
    }

    private int indexOrThrow(CommunityState state, UUID id) {
        int index = state.indexOf(id);
        if (index < 0) {
            throw new IllegalArgumentException("Agent " + id + " not found");
        }
        return index;
    }

    private CommunityState rebuildState(CommunityState base,
                                        double[][] matrix,
                                        ContributionResult result,
                                        List<Transaction> transactions,
                                        Instant timestamp) {
        int size = matrix.length;
        double[] contributions = result.contributions();
        double[] power = result.purchasingPower();
        if (contributions.length != size || power.length != size) {
            throw new IllegalStateException("Inconsistent vector lengths while rebuilding state");
        }
        return CommunityState.builder(size)
                .agents(base.getAgents())
                .evaluationMatrix(matrix)
                .contributions(contributions)
                .purchasingPower(power)
                .transactions(transactions)
                .recoveryConfig(base.getRecoveryConfig())
                .lastUpdated(timestamp)
                .build();
    }
}
