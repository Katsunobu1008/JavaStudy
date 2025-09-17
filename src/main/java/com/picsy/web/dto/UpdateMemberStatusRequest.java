package com.picsy.web.dto;

import com.picsy.domain.AgentStatus;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UpdateMemberStatusRequest(
        @NotNull UUID memberId,
        @NotNull AgentStatus status
) {}
