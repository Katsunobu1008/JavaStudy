package com.picsy.domain.matrix;

/**
 * Convenience payload for synchronising matrix state with the client UI.
 */
public record MatrixView(double[] contributions, double[] purchasingPower, double[][] evaluationMatrix) {
    public MatrixView {
        if (contributions == null || purchasingPower == null || evaluationMatrix == null) {
            throw new IllegalArgumentException("MatrixView components must be non-null");
        }
    }
}
