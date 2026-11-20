
package com.sivasaran.ledger.account;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;


import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "accounts")
public class Account {

    public Account(String currency) {
        this.id = UUID.randomUUID();
        this.currency = currency;
        this.balancePaise = 0;
        this.createdAt = OffsetDateTime.now();
    }

    @Id
    private UUID id;

    @Column(nullable = false, length = 3)
    private String currency;

    @Column(name = "balance_paise", nullable = false)
    private long balancePaise;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    protected Account() {
        // Required by JPA
    }


    public void debit(long amountPaise) {
        if (amountPaise <= 0) {
            throw new IllegalArgumentException(
                    "Debit amount must be positive");
        }

        if (balancePaise < amountPaise) {
            throw new IllegalArgumentException(
                    "Insufficient balance");
        }

        balancePaise -= amountPaise;
    }

    public void credit(long amountPaise) {
        if (amountPaise <= 0) {
            throw new IllegalArgumentException(
                    "Credit amount must be positive");
        }

        balancePaise = Math.addExact(balancePaise, amountPaise);
    }

    public UUID getId() {
        return id;
    }

    public String getCurrency() {
        return currency;
    }

    public long getBalancePaise() {
        return balancePaise;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }
}
