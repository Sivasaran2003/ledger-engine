
package com.sivasaran.ledger.controller;

import com.sivasaran.ledger.account.Account;
import com.sivasaran.ledger.service.AccountService;
import com.sivasaran.ledger.transaction.LedgerEntryResponse;
import com.sivasaran.ledger.transaction.LedgerHistoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private final AccountService accountService;
    private final LedgerHistoryService ledgerHistoryService;

    public AccountController(AccountService accountService, LedgerHistoryService ledgerHistoryService) {
        this.accountService = accountService;
        this.ledgerHistoryService = ledgerHistoryService;
    }

    @PostMapping
    public CreateAccountResponse createAccount(
            @RequestBody CreateAccountRequest request) {

        UUID accountId = accountService.createAccount(request.currency());
        return new CreateAccountResponse(accountId);
    }

    @GetMapping("/{id}")
    public AccountResponse getAccount(@PathVariable UUID id) {
        Account account = accountService.getAccount(id);

        return new AccountResponse(
                account.getId(),
                account.getCurrency(),
                account.getBalancePaise(),
                account.getCreatedAt()
        );
    }

    @GetMapping("/{accountId}/ledger")
    public Page<LedgerEntryResponse> getLedgerHistory(
            @PathVariable UUID accountId,
            @PageableDefault(size = 20) Pageable pageable) {
        return ledgerHistoryService.getLedgerHistory(accountId, pageable);
    }

    public record CreateAccountRequest(String currency) {}

    public record CreateAccountResponse(UUID id) {}

    public record AccountResponse(
            UUID id,
            String currency,
            long balancePaise,
            java.time.OffsetDateTime createdAt
    ) {}
}
