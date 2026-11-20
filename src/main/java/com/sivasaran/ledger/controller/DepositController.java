
package com.sivasaran.ledger.controller;

import com.sivasaran.ledger.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/accounts")
public class DepositController {

    private final AccountService accountService;

    public DepositController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/{accountId}/deposits")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deposit(
            @PathVariable UUID accountId,
            @RequestBody DepositRequest request) {

        accountService.deposit(accountId, request.amountPaise());
    }

    public record DepositRequest(long amountPaise) {}
}
