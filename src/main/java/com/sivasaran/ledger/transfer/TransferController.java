
package com.sivasaran.ledger.transfer;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/transfers")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransferResponse transfer(@RequestBody TransferRequest request) {
        UUID transactionId = transferService.transfer(
                request.fromAccountId(),
                request.toAccountId(),
                request.amountPaise(),
                request.reference()
        );

        return new TransferResponse(transactionId, request.reference());
    }

    public record TransferRequest(
            UUID fromAccountId,
            UUID toAccountId,
            long amountPaise,
            String reference
    ) {}

    public record TransferResponse(
            UUID transactionId,
            String reference
    ) {}
}
