package com.picsy.web.dto;

import com.picsy.domain.Transaction;

import java.time.Instant;
import java.util.UUID;

public record TransactionView(
        UUID buyerId,
        UUID sellerId,
        double amount,
        Instant occurredAt,
        String note
) {
    public static TransactionView fromDomain(Transaction transaction) {
        return new TransactionView(
                transaction.buyerId(),
                transaction.sellerId(),
                transaction.amount(),
                transaction.occurredAt(),
                transaction.note()
        );
    }
}
