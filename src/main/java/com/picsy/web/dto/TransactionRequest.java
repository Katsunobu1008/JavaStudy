package com.picsy.web.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.util.UUID;

public record TransactionRequest(
        @NotNull UUID buyerId,
        @NotNull UUID sellerId,
        @PositiveOrZero double amount,
        String note
) {}
