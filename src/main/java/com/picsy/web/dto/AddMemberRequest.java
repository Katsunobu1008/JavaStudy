package com.picsy.web.dto;

import com.picsy.domain.AgentType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Map;

public record AddMemberRequest(
        @NotBlank String name,
        @NotNull AgentType type,
        Map<String, String> metadata
) {}
