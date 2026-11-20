package com.sivasaran.ledger.transaction;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TransactionQueryService {

    private final TransactionRepository transactionRepository;

    public TransactionQueryService(
            TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public TransactionResponse getTransaction(UUID transactionId) {
        LedgerTransaction transaction = transactionRepository
                .findById(transactionId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Transaction not found: " + transactionId));

        return TransactionResponse.from(transaction);
    }
}