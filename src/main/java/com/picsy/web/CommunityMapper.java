package com.picsy.web;

import com.picsy.domain.CommunityState;
import com.picsy.web.dto.AgentView;
import com.picsy.web.dto.CommunitySnapshotResponse;
import com.picsy.web.dto.TransactionView;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CommunityMapper {

    public CommunitySnapshotResponse toResponse(CommunityState state) {
        List<AgentView> agents = state.getAgents().stream().map(AgentView::fromDomain).toList();
        List<TransactionView> transactions = state.getTransactions().stream()
                .map(TransactionView::fromDomain)
                .collect(Collectors.toList());
        Duration interval = state.getRecoveryConfig().getInterval();
        long intervalSeconds = interval == null ? 0L : interval.toSeconds();
        return new CommunitySnapshotResponse(
                agents,
                state.getContributions(),
                state.getPurchasingPower(),
                state.getEvaluationMatrix(),
                transactions,
                state.getRecoveryConfig().getGamma(),
                intervalSeconds,
                state.getLastUpdated()
        );
    }
}
