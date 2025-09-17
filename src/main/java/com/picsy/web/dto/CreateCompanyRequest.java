package com.picsy.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public record CreateCompanyRequest(
        @NotBlank String name,
        List<UUID> founders,
        Map<String, String> metadata
) {}
