package com.sivasaran.ledger.transaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LedgerHistoryService {

    private final LedgerEntryRepository entryRepository;

    public LedgerHistoryService(
            LedgerEntryRepository entryRepository) {
        this.entryRepository = entryRepository;
    }

    public Page<LedgerEntryResponse> getLedgerHistory(
            UUID accountId,
            Pageable pageable) {

        return entryRepository
                .findByAccountIdOrderByCreatedAtDesc(
                        accountId,
                        pageable)
                .map(LedgerEntryResponse::from);
    }
}