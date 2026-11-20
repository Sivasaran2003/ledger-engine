
package com.sivasaran.ledger.transfer;

import com.sivasaran.ledger.idempotency.IdempotencyService;
import com.sivasaran.ledger.transaction.LedgerTransaction;
import com.sivasaran.ledger.transaction.LedgerTransactionRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TransferService {

    private final LedgerTransactionRepository transactionRepository;
    private final TransferExecutionService transferExecutionService;
    private final IdempotencyService idempotencyService;

    public TransferService(
            LedgerTransactionRepository transactionRepository,
            TransferExecutionService transferExecutionService, IdempotencyService idempotencyService) {
        this.transactionRepository = transactionRepository;
        this.transferExecutionService = transferExecutionService;
        this.idempotencyService = idempotencyService;
    }

    public UUID transfer(
            UUID fromAccountId,
            UUID toAccountId,
            long amountPaise,
            String reference) {

        String cachedValue = idempotencyService.get(reference);

        if (cachedValue != null) {
            String[] values = cachedValue.split("\\|");

            UUID cachedFromAccountId = UUID.fromString(values[0]);
            UUID cachedToAccountId = UUID.fromString(values[1]);
            long cachedAmountPaise = Long.parseLong(values[2]);
            UUID cachedTransactionId = UUID.fromString(values[3]);

            boolean sameRequest =
                    fromAccountId.equals(cachedFromAccountId)
                            && toAccountId.equals(cachedToAccountId)
                            && amountPaise == cachedAmountPaise;

            if (sameRequest) {
                return cachedTransactionId;
            }

            throw new IllegalArgumentException(
                    "Transaction reference already used for a different request");
        }

        if (fromAccountId.equals(toAccountId)) {
            throw new IllegalArgumentException(
                    "Source and destination accounts must differ");
        }

        if (amountPaise <= 0) {
            throw new IllegalArgumentException(
                    "Transfer amount must be positive");
        }

        if (reference == null || reference.isBlank()) {
            throw new IllegalArgumentException(
                    "Reference is required");
        }

        var existingTransaction =
                transactionRepository.findByReference(reference);

        if (existingTransaction.isPresent()) {
            LedgerTransaction existing = existingTransaction.get();

            boolean sameRequest =
                    fromAccountId.equals(existing.getFromAccountId())
                            && toAccountId.equals(existing.getToAccountId())
                            && existing.getAmountPaise() != null
                            && amountPaise == existing.getAmountPaise();

            if (sameRequest) {
                return existing.getId();
            }

            throw new IllegalArgumentException(
                    "Transaction reference already used for a different request");
        }

        try {
            UUID transactionId = transferExecutionService.executeTransfer(
                    fromAccountId,
                    toAccountId,
                    amountPaise,
                    reference
            );

            idempotencyService.store(
                    reference,
                    fromAccountId,
                    toAccountId,
                    amountPaise,
                    transactionId
            );

            return transactionId;
        } catch (DataIntegrityViolationException exception) {
            var transaction = transactionRepository.findByReference(reference);

            if (transaction.isEmpty()) {
                throw exception;
            }

            LedgerTransaction existing = transaction.get();

            boolean sameRequest =
                    fromAccountId.equals(existing.getFromAccountId())
                            && toAccountId.equals(existing.getToAccountId())
                            && existing.getAmountPaise() != null
                            && amountPaise == existing.getAmountPaise();

            if (sameRequest) {
                idempotencyService.store(
                        reference,
                        fromAccountId,
                        toAccountId,
                        amountPaise,
                        existing.getId()
                );

                return existing.getId();
            }

            throw new IllegalArgumentException(
                    "Transaction reference already used for a different request");
        }
    }
}
