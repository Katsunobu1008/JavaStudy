package com.picsy.web.dto;

import java.time.Instant;
import java.util.List;

public record CommunitySnapshotResponse(
        List<AgentView> agents,
        double[] contributions,
        double[] purchasingPower,
        double[][] evaluationMatrix,
        List<TransactionView> transactions,
        double naturalRecoveryGamma,
        long naturalRecoveryIntervalSeconds,
        Instant lastUpdated
) {}
