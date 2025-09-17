package com.picsy.domain.matrix;

import java.util.Arrays;

/**
 * Implements a left-eigenvector power method for row-stochastic matrices.
 */
public final class PowerMethodSolver {

    private PowerMethodSolver() {}

    public static double[] solveLeftEigenvector(double[][] rowStochasticMatrix, double tolerance, int maxIterations) {
        int n = rowStochasticMatrix.length;
        double[] current = MatrixUtils.uniformVector(n);
        double[] next = new double[n];

        for (int iteration = 0; iteration < maxIterations; iteration++) {
            Arrays.fill(next, 0d);
            for (int i = 0; i < n; i++) {
                double value = current[i];
                if (value == 0) {
                    continue;
                }
                double[] row = rowStochasticMatrix[i];
                for (int j = 0; j < n; j++) {
                    next[j] += value * row[j];
                }
            }
            double diff = MatrixUtils.difference(current, next);
            if (diff < tolerance) {
                return MatrixUtils.normaliseToSum(next, n);
            }
            System.arraycopy(next, 0, current, 0, n);
        }
        throw new IllegalStateException("Power method did not converge within " + maxIterations + " iterations");
    }
}
