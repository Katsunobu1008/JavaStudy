package com.picsy.domain;

import com.picsy.domain.config.NaturalRecoveryConfig;
import com.picsy.domain.matrix.MatrixView;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Aggregate that keeps every piece of state required to evaluate the PICSY model at a given instant.
 */
public final class CommunityState {

    private final List<Agent> agents;
    private final double[][] evaluationMatrix;
    private final double[] contributions;
    private final double[] purchasingPower;
    private final List<Transaction> transactions;
    private final NaturalRecoveryConfig recoveryConfig;
    private final Instant lastUpdated;

    private CommunityState(Builder builder) {
        this.agents = Collections.unmodifiableList(new ArrayList<>(builder.agents));
        this.evaluationMatrix = copyMatrix(builder.evaluationMatrix);
        this.contributions = builder.contributions.clone();
        this.purchasingPower = builder.purchasingPower.clone();
        this.transactions = Collections.unmodifiableList(new ArrayList<>(builder.transactions));
        this.recoveryConfig = builder.recoveryConfig;
        this.lastUpdated = builder.lastUpdated == null ? Instant.now() : builder.lastUpdated;
    }

    public int size() {
        return agents.size();
    }

    public List<Agent> getAgents() {
        return agents;
    }

    public double[][] getEvaluationMatrix() {
        return copyMatrix(evaluationMatrix);
    }

    public double[] getContributions() {
        return contributions.clone();
    }

    public double[] getPurchasingPower() {
        return purchasingPower.clone();
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public NaturalRecoveryConfig getRecoveryConfig() {
        return recoveryConfig;
    }

    public Instant getLastUpdated() {
        return lastUpdated;
    }

    public MatrixView toMatrixView() {
        return new MatrixView(getContributions(), getPurchasingPower(), getEvaluationMatrix());
    }

    public int indexOf(UUID agentId) {
        for (int i = 0; i < agents.size(); i++) {
            if (agents.get(i).getId().equals(agentId)) {
                return i;
            }
        }
        return -1;
    }

    public static Builder builder(int size) {
        return new Builder(size);
    }

    private static double[][] copyMatrix(double[][] matrix) {
        double[][] copy = new double[matrix.length][];
        for (int i = 0; i < matrix.length; i++) {
            copy[i] = matrix[i].clone();
        }
        return copy;
    }

    public static final class Builder {
        private final List<Agent> agents;
        private final double[][] evaluationMatrix;
        private double[] contributions;
        private double[] purchasingPower;
        private final List<Transaction> transactions = new ArrayList<>();
        private NaturalRecoveryConfig recoveryConfig;
        private Instant lastUpdated;

        private Builder(int size) {
            if (size <= 0) {
                throw new IllegalArgumentException("Community size must be positive");
            }
            this.agents = new ArrayList<>(size);
            this.evaluationMatrix = new double[size][size];
            this.contributions = new double[size];
            this.purchasingPower = new double[size];
        }

        public Builder agents(List<Agent> agents) {
            this.agents.clear();
            this.agents.addAll(agents);
            return this;
        }

        public Builder evaluationMatrix(double[][] matrix) {
            if (matrix.length != evaluationMatrix.length) {
                throw new IllegalArgumentException("Matrix size mismatch");
            }
            for (int i = 0; i < matrix.length; i++) {
                if (matrix[i].length != evaluationMatrix.length) {
                    throw new IllegalArgumentException("Matrix must be square and match community size");
                }
                System.arraycopy(matrix[i], 0, this.evaluationMatrix[i], 0, matrix[i].length);
            }
            return this;
        }

        public Builder contributions(double[] contributions) {
            this.contributions = contributions.clone();
            return this;
        }

        public Builder purchasingPower(double[] purchasingPower) {
            this.purchasingPower = purchasingPower.clone();
            return this;
        }

        public Builder transactions(List<Transaction> transactions) {
            this.transactions.clear();
            this.transactions.addAll(transactions);
            return this;
        }

        public Builder addTransaction(Transaction transaction) {
            this.transactions.add(transaction);
            return this;
        }

        public Builder recoveryConfig(NaturalRecoveryConfig recoveryConfig) {
            this.recoveryConfig = recoveryConfig;
            return this;
        }

        public Builder lastUpdated(Instant lastUpdated) {
            this.lastUpdated = lastUpdated;
            return this;
        }

        public CommunityState build() {
            if (agents.size() != evaluationMatrix.length) {
                throw new IllegalStateException("Agent list size and matrix dimension must match");
            }
            if (recoveryConfig == null) {
                throw new IllegalStateException("Natural recovery configuration must be provided");
            }
            return new CommunityState(this);
        }
    }
}
