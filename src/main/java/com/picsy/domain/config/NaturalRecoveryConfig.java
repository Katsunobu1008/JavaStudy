package com.picsy.domain.config;

import java.time.Duration;

/**
 * Settings that control the natural recovery mechanism (budget replenishment).
 */
public final class NaturalRecoveryConfig {

    private final double gamma;
    private final Duration interval;

    public NaturalRecoveryConfig(double gamma, Duration interval) {
        if (gamma <= 0 || gamma >= 1) {
            throw new IllegalArgumentException("Gamma must be within (0, 1)");
        }
        this.gamma = gamma;
        this.interval = interval == null ? Duration.ofHours(1) : interval;
    }

    public double getGamma() {
        return gamma;
    }

    public Duration getInterval() {
        return interval;
    }

    public NaturalRecoveryConfig withGamma(double newGamma) {
        return new NaturalRecoveryConfig(newGamma, this.interval);
    }

    public NaturalRecoveryConfig withInterval(Duration newInterval) {
        return new NaturalRecoveryConfig(this.gamma, newInterval);
    }
}
