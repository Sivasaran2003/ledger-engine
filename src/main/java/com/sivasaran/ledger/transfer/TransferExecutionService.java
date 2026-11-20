
package com.sivasaran.ledger.transfer;

import com.sivasaran.ledger.account.Account;
import com.sivasaran.ledger.account.AccountRepository;
import com.sivasaran.ledger.outbox.OutboxEvent;
import com.sivasaran.ledger.outbox.OutboxEventRepository;
import com.sivasaran.ledger.transaction.LedgerEntry;
import com.sivasaran.ledger.transaction.LedgerEntryRepository;
import com.sivasaran.ledger.transaction.LedgerTransaction;
import com.sivasaran.ledger.transaction.LedgerTransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class TransferExecutionService {

    private final AccountRepository accountRepository;
    private final LedgerTransactionRepository transactionRepository;
    private final LedgerEntryRepository entryRepository;
    private final OutboxEventRepository outboxEventRepository;

    public TransferExecutionService(
            AccountRepository accountRepository,
            LedgerTransactionRepository transactionRepository,
            LedgerEntryRepository entryRepository, OutboxEventRepository outboxEventRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.entryRepository = entryRepository;
        this.outboxEventRepository = outboxEventRepository;
    }

    @Transactional
    public UUID executeTransfer(
            UUID fromAccountId,
            UUID toAccountId,
            long amountPaise,
            String reference) {

        UUID firstId = fromAccountId.compareTo(toAccountId) < 0
                ? fromAccountId : toAccountId;
        UUID secondId = fromAccountId.compareTo(toAccountId) < 0
                ? toAccountId : fromAccountId;

        Account first = accountRepository.findByIdForUpdate(firstId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Account not found: " + firstId));

        Account second = accountRepository.findByIdForUpdate(secondId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Account not found: " + secondId));

        Account source = firstId.equals(fromAccountId) ? first : second;
        Account destination = firstId.equals(toAccountId) ? first : second;

        if (!source.getCurrency().equals(destination.getCurrency())) {
            throw new IllegalArgumentException(
                    "Account currencies must match");
        }

        if (source.getBalancePaise() < amountPaise) {
            throw new IllegalArgumentException("Insufficient balance");
        }

        source.debit(amountPaise);
        destination.credit(amountPaise);

        LedgerTransaction transaction = new LedgerTransaction(
                reference,
                source.getId(),
                destination.getId(),
                amountPaise
        );

        transactionRepository.saveAndFlush(transaction);

        entryRepository.save(new LedgerEntry(
                transaction.getId(), source.getId(), "DEBIT", amountPaise));

        entryRepository.save(new LedgerEntry(
                transaction.getId(), destination.getId(), "CREDIT", amountPaise));

        String payload = """
        {
          "transactionId": "%s",
          "reference": "%s",
          "fromAccountId": "%s",
          "toAccountId": "%s",
          "amountPaise": %d
        }
        """.formatted(
                transaction.getId(),
                transaction.getReference(),
                source.getId(),
                destination.getId(),
                amountPaise
        );

        OutboxEvent event = new OutboxEvent(
                "TRANSFER_COMPLETED",
                transaction.getId(),
                payload
        );

        outboxEventRepository.save(event);

        return transaction.getId();
    }
}
