
package com.sivasaran.ledger.service;

import com.sivasaran.ledger.account.Account;
import com.sivasaran.ledger.account.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.UUID;

@Service
public class AccountService {

    private static final Set<String> SUPPORTED_CURRENCIES =
            Set.of("INR", "USD");

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public UUID createAccount(String currency) {
        if (currency == null) {
            throw new IllegalArgumentException("Currency is required");
        }

        String normalizedCurrency = currency.trim().toUpperCase();

        if (!SUPPORTED_CURRENCIES.contains(normalizedCurrency)) {
            throw new IllegalArgumentException(
                    "Unsupported currency: " + normalizedCurrency);
        }

        Account account = new Account(normalizedCurrency);

        return accountRepository.save(account).getId();
    }


    @Transactional(readOnly = true)
    public Account getAccount(UUID accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Account not found: " + accountId));
    }

    @Transactional
    public void deposit(UUID accountId, long amountPaise) {
        if (amountPaise <= 0) {
            throw new IllegalArgumentException(
                    "Deposit amount must be positive"
            );
        }

        Account account = accountRepository.findByIdForUpdate(accountId)
                .orElseThrow(() ->
                        new IllegalArgumentException("Account not found"));

        account.credit(amountPaise);
    }

}
