package com.picsy.domain;

import java.time.Instant;
import java.util.UUID;

/**
 * Immutable representation of a single trade between two agents.
 */
public record Transaction(
        UUID buyerId,
        UUID sellerId,
        double amount,
        Instant occurredAt,
        String note
) {
    public Transaction {
        if (amount < 0) {
            throw new IllegalArgumentException("Transaction amount must be non-negative");
        }
        if (buyerId == null || sellerId == null) {
            throw new IllegalArgumentException("Buyer and seller identifiers are required");
        }
    }

    public static Transaction of(UUID buyerId, UUID sellerId, double amount, String note) {
        return new Transaction(buyerId, sellerId, amount, Instant.now(), note);
    }
}
