package com.sivasaran.ledger.webhook;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/webhooks")
public class WebhookController {

    private final ReceivedEventRepository receivedEventRepository;

    public WebhookController(
            ReceivedEventRepository receivedEventRepository) {
        this.receivedEventRepository = receivedEventRepository;
    }

    @PostMapping("/events")
    public ResponseEntity<Void> receiveEvent(
            @RequestBody WebhookEventRequest request) {

        try {
            receivedEventRepository.saveAndFlush(
                    new ReceivedEvent(
                            request.eventId(),
                            request.eventType(),
                            request.payload()
                    )
            );

            return ResponseEntity.ok().build();

        } catch (DataIntegrityViolationException exception) {
            // Event was already received — treat duplicate delivery as success.
            return ResponseEntity.ok().build();
        }
    }

    public record WebhookEventRequest(
            UUID eventId,
            String eventType,
            String payload) {
    }
}