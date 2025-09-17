package com.picsy.domain;

import com.picsy.domain.config.NaturalRecoveryConfig;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Produces initialised community states for a brand-new PICSY simulation.
 */
public final class CommunityFactory {

    private CommunityFactory() {}

    public static CommunityState createUniformCommunity(int size) {
        if (size <= 0) {
            throw new IllegalArgumentException("Community size must be positive");
        }
        List<Agent> agents = new ArrayList<>(size);
        double[][] matrix = new double[size][size];
        double equalShare = 1d / size;

        for (int i = 0; i < size; i++) {
            agents.add(Agent.builder()
                    .id(UUID.randomUUID())
                    .name("Member " + (i + 1))
                    .type(AgentType.PERSON)
                    .build());
            for (int j = 0; j < size; j++) {
                matrix[i][j] = equalShare;
            }
        }

        double[] contributions = new double[size];
        double[] purchasingPower = new double[size];
        for (int i = 0; i < size; i++) {
            contributions[i] = 1d;
            purchasingPower[i] = equalShare; // initial budget equals self-evaluation * contribution
        }

        return CommunityState.builder(size)
                .agents(agents)
                .evaluationMatrix(matrix)
                .contributions(contributions)
                .purchasingPower(purchasingPower)
                .recoveryConfig(new NaturalRecoveryConfig(0.05d, Duration.ofHours(1)))
                .build();
    }
}
