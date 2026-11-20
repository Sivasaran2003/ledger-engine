package com.sivasaran.ledger.transaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface LedgerEntryRepository
        extends JpaRepository<LedgerEntry, UUID> {

    List<LedgerEntry> findByTransactionId(UUID transactionId);

    Page<LedgerEntry> findByAccountIdOrderByCreatedAtDesc(
            UUID accountId,
            Pageable pageable
    );
}