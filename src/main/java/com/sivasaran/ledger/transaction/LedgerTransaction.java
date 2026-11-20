
package com.sivasaran.ledger.transaction;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "ledger_transactions")
public class LedgerTransaction {

    @Id
    private UUID id;

    @Column(nullable = false, unique = true)
    private String reference;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    @Column(name = "from_account_id")
    private UUID fromAccountId;

    @Column(name = "to_account_id")
    private UUID toAccountId;

    @Column(name = "amount_paise")
    private Long amountPaise;

    @Column(nullable = false)
    private OffsetDateTime createdAt;

    protected LedgerTransaction() {
    }

    public LedgerTransaction(
            String reference,
            UUID fromAccountId,
            UUID toAccountId,
            long amountPaise) {
        this.id = UUID.randomUUID();
        this.reference = reference;
        this.status = TransactionStatus.COMPLETED;
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.amountPaise = amountPaise;
        this.createdAt = OffsetDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public String getReference() {
        return reference;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public UUID getFromAccountId() {
        return fromAccountId;
    }

    public UUID getToAccountId() {
        return toAccountId;
    }

    public Long getAmountPaise() {
        return amountPaise;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
