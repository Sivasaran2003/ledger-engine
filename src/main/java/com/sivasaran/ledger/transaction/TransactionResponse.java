package com.sivasaran.ledger.transaction;

import java.time.OffsetDateTime;
import java.util.UUID;

public record TransactionResponse(
        UUID transactionId,
        String reference,
        TransactionStatus status,
        UUID fromAccountId,
        UUID toAccountId,
        Long amountPaise,
        OffsetDateTime createdAt
) {
    public static TransactionResponse from(LedgerTransaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getReference(),
                transaction.getStatus(),
                transaction.getFromAccountId(),
                transaction.getToAccountId(),
                transaction.getAmountPaise(),
                transaction.getCreatedAt()
        );
    }
}