package com.picsy.domain.matrix;

/**
 * Encapsulates the outcome of a contribution recalculation.
 */
public record ContributionResult(double[] contributions, double[] purchasingPower) {
    public ContributionResult {
        if (contributions == null || purchasingPower == null) {
            throw new IllegalArgumentException("Result vectors must be non-null");
        }
        if (contributions.length != purchasingPower.length) {
            throw new IllegalArgumentException("Contribution and purchasing power vectors must have the same length");
        }
    }
}
