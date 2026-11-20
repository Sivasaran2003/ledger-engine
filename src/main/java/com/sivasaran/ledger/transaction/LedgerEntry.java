
package com.sivasaran.ledger.transaction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "ledger_entries")
public class LedgerEntry {

    @Id
    private UUID id;

    @Column(name = "transaction_id", nullable = false)
    private UUID transactionId;

    @Column(name = "account_id", nullable = false)
    private UUID accountId;

    @Column(name = "entry_type", nullable = false, length = 10)
    private String entryType;

    @Column(name = "amount_paise", nullable = false)
    private long amountPaise;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    protected LedgerEntry() {
        // Required by JPA
    }

    public LedgerEntry(
            UUID transactionId,
            UUID accountId,
            String entryType,
            long amountPaise) {

        this.id = UUID.randomUUID();
        this.transactionId = transactionId;
        this.accountId = accountId;
        this.entryType = entryType;
        this.amountPaise = amountPaise;
        this.createdAt = OffsetDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public String getEntryType() {
        return entryType;
    }

    public long getAmountPaise() {
        return amountPaise;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
