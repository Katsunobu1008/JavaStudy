package com.picsy.web.dto;

import com.picsy.domain.Agent;
import com.picsy.domain.AgentStatus;
import com.picsy.domain.AgentType;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public record AgentView(
        UUID id,
        String name,
        AgentType type,
        AgentStatus status,
        Instant createdAt,
        Map<String, String> metadata
) {
    public static AgentView fromDomain(Agent agent) {
        return new AgentView(
                agent.getId(),
                agent.getName(),
                agent.getType(),
                agent.getStatus(),
                agent.getCreatedAt(),
                agent.getMetadata()
        );
    }
}
