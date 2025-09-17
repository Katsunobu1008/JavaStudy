package com.picsy.domain.matrix;

/**
 * Produces the virtual-central-bank transformed matrix E' used for contribution calculations.
 */
public final class VirtualCentralBankTransformer {

    private VirtualCentralBankTransformer() {}

    public static double[][] transform(double[][] evaluationMatrix) {
        int n = evaluationMatrix.length;
        double[][] transformed = new double[n][n];
        for (int i = 0; i < n; i++) {
            double selfAssessment = evaluationMatrix[i][i];
            double redistribution = n > 1 ? selfAssessment / (n - 1) : 0d;
            for (int j = 0; j < n; j++) {
                if (i == j) {
                    transformed[i][j] = 0d;
                } else {
                    transformed[i][j] = evaluationMatrix[i][j] + redistribution;
                }
            }
        }
        return transformed;
    }
}
