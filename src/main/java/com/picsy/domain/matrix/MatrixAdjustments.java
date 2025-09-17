package com.picsy.domain.matrix;

/**
 * Applies model-specific matrix adjustments such as natural recovery and transactions.
 */
public final class MatrixAdjustments {

    private MatrixAdjustments() {}

    public static double[][] applyNaturalRecovery(double[][] matrix, double gamma) {
        int n = matrix.length;
        double[][] adjusted = MatrixUtils.copy(matrix);
        double retainFactor = 1d - gamma;
        for (int i = 0; i < n; i++) {
            double diagonal = adjusted[i][i];
            double newDiagonal = diagonal + gamma * (1d - diagonal);
            adjusted[i][i] = newDiagonal;
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    continue;
                }
                adjusted[i][j] *= retainFactor;
            }
        }
        return adjusted;
    }

    public static double[][] applyTransaction(double[][] matrix, int buyerIndex, int sellerIndex, double amount) {
        double[][] updated = MatrixUtils.copy(matrix);
        double buyerBudget = updated[buyerIndex][buyerIndex];
        if (amount < 0) {
            throw new IllegalArgumentException("Transaction amount must be non-negative");
        }
        if (amount > buyerBudget + 1e-9) {
            throw new IllegalArgumentException("Buyer does not have enough budget for this transaction");
        }
        updated[buyerIndex][buyerIndex] = buyerBudget - amount;
        updated[buyerIndex][sellerIndex] += amount;
        return updated;
    }
}
