package com.picsy.service;

import com.picsy.domain.Agent;
import com.picsy.domain.AgentStatus;
import com.picsy.domain.AgentType;
import com.picsy.domain.CommunityState;
import com.picsy.domain.Transaction;
import com.picsy.domain.config.NaturalRecoveryConfig;
import com.picsy.domain.matrix.ContributionCalculator;
import com.picsy.domain.matrix.ContributionResult;
import com.picsy.domain.matrix.MatrixAdjustments;
import com.picsy.domain.matrix.MatrixUtils;
import com.picsy.web.CommunityEventPublisher;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class CommunityService {

    private static final double DECEASED_PRUNE_THRESHOLD = 1e-6;

    private final ContributionCalculator contributionCalculator;
    private final CommunityEventPublisher eventPublisher;
    private final AtomicReference<CommunityState> stateRef;

    public CommunityService(CommunityState seedState, ContributionCalculator contributionCalculator, CommunityEventPublisher eventPublisher) {
        this.contributionCalculator = contributionCalculator;
        this.eventPublisher = eventPublisher;
        ContributionResult initialResult = contributionCalculator.recalculate(seedState.getEvaluationMatrix());
        CommunityState recalculated = rebuildState(
                seedState.getAgents(),
                seedState.getEvaluationMatrix(),
                initialResult,
                seedState.getTransactions(),
                seedState.getRecoveryConfig(),
                Instant.now()
        );
        this.stateRef = new AtomicReference<>(recalculated);
    }

    public CommunityState snapshot() {
        return stateRef.get();
    }

    public CommunityState addCompany(String name, List<UUID> founderIds, Map<String, String> metadata) {
        return stateRef.updateAndGet(current -> {
            int size = current.size();
            List<Agent> agents = new ArrayList<>(current.getAgents());
            Agent newCompany = Agent.builder()
                    .name(name)
                    .type(AgentType.COMPANY)
                    .metadata(metadata == null ? Map.of() : Map.copyOf(metadata))
                    .build();
            agents.add(newCompany);

            double[][] expandedMatrix = new double[size + 1][size + 1];
            double[][] original = current.getEvaluationMatrix();
            for (int i = 0; i < size; i++) {
                System.arraycopy(original[i], 0, expandedMatrix[i], 0, size);
                expandedMatrix[i][size] = 0d;
            }

            double[] companyRow = new double[size + 1];
            List<UUID> founders = founderIds == null ? List.of() : founderIds;
            if (!founders.isEmpty()) {
                double share = 1d / founders.size();
                for (UUID founderId : founders) {
                    int founderIndex = indexOrThrow(current, founderId);
                    companyRow[founderIndex] = share;
                }
            } else if (size > 0) {
                double share = 1d / size;
                for (int j = 0; j < size; j++) {
                    companyRow[j] = share;
                }
            } else {
                companyRow[size] = 1d;
            }
            if (size > 0) {
                companyRow[size] = 0d;
            }
            System.arraycopy(companyRow, 0, expandedMatrix[size], 0, size + 1);

            MatrixUtils.validateRowStochastic(expandedMatrix, 1e-9);
            ContributionResult result = contributionCalculator.recalculate(expandedMatrix);
            return rebuildState(agents, expandedMatrix, result, current.getTransactions(), current.getRecoveryConfig(), Instant.now());
        });
    }

    public CommunityState addMember(String name, AgentType type, Map<String, String> metadata) {
        return stateRef.updateAndGet(current -> {
            int size = current.size();
            List<Agent> agents = new ArrayList<>(current.getAgents());
            Agent newAgent = Agent.builder()
                    .name(name)
                    .type(type)
                    .metadata(metadata == null ? Map.of() : Map.copyOf(metadata))
                    .build();
            agents.add(newAgent);

            double[][] expandedMatrix = new double[size + 1][size + 1];
            double[][] original = current.getEvaluationMatrix();
            for (int i = 0; i < size; i++) {
                System.arraycopy(original[i], 0, expandedMatrix[i], 0, size);
                expandedMatrix[i][size] = 0d;
            }
            double share = 1d / (size + 1);
            for (int j = 0; j < size + 1; j++) {
                expandedMatrix[size][j] = share;
            }

            MatrixUtils.validateRowStochastic(expandedMatrix, 1e-9);
            ContributionResult result = contributionCalculator.recalculate(expandedMatrix);
            return rebuildState(agents, expandedMatrix, result, current.getTransactions(), current.getRecoveryConfig(), Instant.now());
        });
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
            return rebuildState(current.getAgents(), updatedMatrix, result, transactions, current.getRecoveryConfig(), Instant.now());
        });
    }

    public CommunityState applyNaturalRecovery() {
        return stateRef.updateAndGet(current -> {
            double gamma = current.getRecoveryConfig().getGamma();
            boolean[] activeFlags = new boolean[current.size()];
            for (int i = 0; i < current.size(); i++) {
                activeFlags[i] = current.getAgents().get(i).getStatus() == AgentStatus.ACTIVE;
            }
            double[][] adjustedMatrix = MatrixAdjustments.applyNaturalRecovery(current.getEvaluationMatrix(), gamma, activeFlags);
            ContributionResult result = contributionCalculator.recalculate(adjustedMatrix);
            CommunityState updated = rebuildState(current.getAgents(), adjustedMatrix, result, current.getTransactions(), current.getRecoveryConfig(), Instant.now());
            return pruneDeceasedMembers(updated, DECEASED_PRUNE_THRESHOLD);
        });
    }

    public CommunityState updateRecoveryConfig(double gamma, Duration intervalOverride) {
        return stateRef.updateAndGet(current -> {
            Duration effectiveInterval = intervalOverride != null ? intervalOverride : current.getRecoveryConfig().getInterval();
            NaturalRecoveryConfig newConfig = new NaturalRecoveryConfig(gamma, effectiveInterval);
            return rebuildState(
                    current.getAgents(),
                    current.getEvaluationMatrix(),
                    new ContributionResult(current.getContributions(), current.getPurchasingPower()),
                    current.getTransactions(),
                    newConfig,
                    Instant.now()
            );
        });
    }

    public CommunityState updateMemberStatus(UUID memberId, AgentStatus status) {
        Objects.requireNonNull(memberId, "memberId");
        Objects.requireNonNull(status, "status");
        return stateRef.updateAndGet(current -> {
            List<Agent> agents = new ArrayList<>(current.getAgents());
            int index = indexOrThrow(current, memberId);
            agents.set(index, agents.get(index).withStatus(status));
            CommunityState intermediate = rebuildState(
                    agents,
                    current.getEvaluationMatrix(),
                    new ContributionResult(current.getContributions(), current.getPurchasingPower()),
                    current.getTransactions(),
                    current.getRecoveryConfig(),
                    Instant.now()
            );
            if (status == AgentStatus.DECEASED) {
                return pruneDeceasedMembers(intermediate, DECEASED_PRUNE_THRESHOLD);
            }
            return intermediate;
        });
    }

    public CommunityState updateEvaluationRow(int rowIndex, double[] values) {
        return stateRef.updateAndGet(current -> {
            if (rowIndex < 0 || rowIndex >= current.size()) {
                throw new IllegalArgumentException("Row index out of range");
            }
            if (values.length != current.size()) {
                throw new IllegalArgumentException("Row length must match community size");
            }
            double[][] matrix = MatrixUtils.copy(current.getEvaluationMatrix());
            System.arraycopy(values, 0, matrix[rowIndex], 0, values.length);
            MatrixUtils.validateRowStochastic(matrix, 1e-9);
            ContributionResult result = contributionCalculator.recalculate(matrix);
            return rebuildState(
                    current.getAgents(),
                    matrix,
                    result,
                    current.getTransactions(),
                    current.getRecoveryConfig(),
                    Instant.now()
            );
        });
    }

    public CommunityState virtualDissolveCompany(UUID companyId) {
        return stateRef.updateAndGet(current -> {
            int companyIndex = indexOrThrow(current, companyId);
            Agent company = current.getAgents().get(companyIndex);
            if (company.getType() != AgentType.COMPANY) {
                throw new IllegalArgumentException("Agent " + companyId + " is not a company");
            }
            double[][] matrix = current.getEvaluationMatrix();
            double selfWeight = matrix[companyIndex][companyIndex];
            if (selfWeight >= 1d - 1e-9) {
                throw new IllegalStateException("Company self-evaluation too high to dissolve");
            }
            double denom = 1d - selfWeight;
            int size = current.size();
            double[] hatRow = new double[size];
            for (int j = 0; j < size; j++) {
                if (j == companyIndex) {
                    continue;
                }
                hatRow[j] = matrix[companyIndex][j] / denom;
            }

            List<Integer> keep = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                if (i != companyIndex) {
                    keep.add(i);
                }
            }
            double[][] newMatrix = new double[keep.size()][keep.size()];
            for (int row = 0; row < keep.size(); row++) {
                int origRow = keep.get(row);
                for (int col = 0; col < keep.size(); col++) {
                    int origCol = keep.get(col);
                    double value = matrix[origRow][origCol] + matrix[origRow][companyIndex] * hatRow[origCol];
                    newMatrix[row][col] = value;
                }
            }
            MatrixUtils.validateRowStochastic(newMatrix, 1e-9);
            List<Agent> newAgents = new ArrayList<>(keep.size());
            for (int idx : keep) {
                newAgents.add(current.getAgents().get(idx));
            }
            ContributionResult result = contributionCalculator.recalculate(newMatrix);
            return rebuildState(newAgents, newMatrix, result, current.getTransactions(), current.getRecoveryConfig(), Instant.now());
        });
    }

    private CommunityState pruneDeceasedMembers(CommunityState state, double threshold) {
        double[] contributions = state.getContributions();
        List<Agent> agents = state.getAgents();
        List<Integer> keep = new ArrayList<>();
        for (int i = 0; i < agents.size(); i++) {
            Agent agent = agents.get(i);
            if (agent.getStatus() == AgentStatus.DECEASED && contributions[i] <= threshold) {
                continue;
            }
            keep.add(i);
        }
        if (keep.size() == agents.size()) {
            return state;
        }
        double[][] originalMatrix = state.getEvaluationMatrix();
        double[][] newMatrix = new double[keep.size()][keep.size()];
        List<Agent> newAgents = new ArrayList<>(keep.size());
        for (int row = 0; row < keep.size(); row++) {
            int origRow = keep.get(row);
            newAgents.add(agents.get(origRow));
            for (int col = 0; col < keep.size(); col++) {
                int origCol = keep.get(col);
                newMatrix[row][col] = originalMatrix[origRow][origCol];
            }
        }
        MatrixUtils.validateRowStochastic(newMatrix, 1e-9);
        ContributionResult result = contributionCalculator.recalculate(newMatrix);
        return rebuildState(newAgents, newMatrix, result, state.getTransactions(), state.getRecoveryConfig(), Instant.now());
    }

    private int indexOrThrow(CommunityState state, UUID id) {
        int index = state.indexOf(id);
        if (index < 0) {
            throw new IllegalArgumentException("Agent " + id + " not found");
        }
        return index;
    }

    private CommunityState rebuildState(List<Agent> agents,
                                        double[][] matrix,
                                        ContributionResult result,
                                        List<Transaction> transactions,
                                        NaturalRecoveryConfig recoveryConfig,
                                        Instant timestamp) {
        int size = matrix.length;
        double[] contributions = result.contributions();
        double[] power = result.purchasingPower();
        if (contributions.length != size || power.length != size) {
            throw new IllegalStateException("Inconsistent vector lengths while rebuilding state");
        }
        return CommunityState.builder(size)
                .agents(agents)
                .evaluationMatrix(matrix)
                .contributions(contributions)
                .purchasingPower(power)
                .transactions(transactions)
                .recoveryConfig(recoveryConfig)
                .lastUpdated(timestamp)
                .build();
    }
}
















