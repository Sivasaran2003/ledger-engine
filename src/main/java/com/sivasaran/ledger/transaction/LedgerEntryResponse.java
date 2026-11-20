package com.sivasaran.ledger.transaction;

import java.time.OffsetDateTime;
import java.util.UUID;

public record LedgerEntryResponse(
        UUID entryId,
        UUID transactionId,
        String entryType,
        long amountPaise,
        OffsetDateTime createdAt
) {
    public static LedgerEntryResponse from(LedgerEntry entry) {
        return new LedgerEntryResponse(
                entry.getId(),
                entry.getTransactionId(),
                entry.getEntryType(),
                entry.getAmountPaise(),
                entry.getCreatedAt()
        );
    }
}