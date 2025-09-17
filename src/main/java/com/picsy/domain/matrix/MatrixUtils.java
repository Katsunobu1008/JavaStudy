package com.picsy.domain.matrix;

import java.util.Arrays;

/**
 * Collection of helper methods to work with evaluation matrices and vectors.
 */
public final class MatrixUtils {

    private MatrixUtils() {}

    public static double[][] copy(double[][] matrix) {
        double[][] result = new double[matrix.length][];
        for (int i = 0; i < matrix.length; i++) {
            result[i] = matrix[i].clone();
        }
        return result;
    }

    public static double[] uniformVector(int size) {
        double[] vector = new double[size];
        Arrays.fill(vector, 1.0d / size);
        return vector;
    }

    /**
     * Ensures all values are non-negative and each row sums to 1 within a tolerance.
     */
    public static void validateRowStochastic(double[][] matrix, double tolerance) {
        for (int i = 0; i < matrix.length; i++) {
            double rowSum = 0d;
            for (int j = 0; j < matrix[i].length; j++) {
                double value = matrix[i][j];
                if (value < -tolerance) {
                    throw new IllegalArgumentException("Matrix contains negative value at (" + i + ", " + j + ")");
                }
                rowSum += value;
            }
            if (Math.abs(rowSum - 1d) > tolerance) {
                throw new IllegalArgumentException("Row " + i + " does not sum to 1 (" + rowSum + ")");
            }
        }
    }

    public static double[] normaliseToSum(double[] vector, double targetSum) {
        double currentSum = Arrays.stream(vector).sum();
        double scale = currentSum == 0 ? 0 : targetSum / currentSum;
        double[] result = vector.clone();
        for (int i = 0; i < result.length; i++) {
            result[i] *= scale;
        }
        return result;
    }

    public static double difference(double[] a, double[] b) {
        double sum = 0d;
        for (int i = 0; i < a.length; i++) {
            double diff = a[i] - b[i];
            sum += diff * diff;
        }
        return Math.sqrt(sum);
    }
}
