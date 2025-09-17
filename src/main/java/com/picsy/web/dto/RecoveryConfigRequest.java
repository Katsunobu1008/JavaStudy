package com.picsy.web.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record RecoveryConfigRequest(
        @NotNull @Positive @DecimalMax(value = "1.0", inclusive = false) double gamma,
        @Positive Long intervalSeconds
) {}
