package com.sivasaran.ledger.transaction;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TransactionRepository
        extends JpaRepository<LedgerTransaction, UUID> {

    Optional<LedgerTransaction> findByReference(String reference);
}