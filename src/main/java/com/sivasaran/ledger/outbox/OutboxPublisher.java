package com.sivasaran.ledger.outbox;

import com.sivasaran.ledger.webhook.WebhookController.WebhookEventRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Component
public class OutboxPublisher {

    private static final String WEBHOOK_URL =
            "http://localhost:8080/api/v1/webhooks/events";

    private final OutboxEventRepository outboxEventRepository;
    private final RestClient restClient;

    public OutboxPublisher(
            OutboxEventRepository outboxEventRepository) {

        this.outboxEventRepository = outboxEventRepository;
        this.restClient = RestClient.create();
    }

    @Scheduled(fixedDelay = 5000)
    public void publishPendingEvents() {

        List<OutboxEvent> events =
                outboxEventRepository
                        .findTop100ByStatusOrderByCreatedAtAsc("PENDING");

        for (OutboxEvent event : events) {
            publish(event);
        }
    }

    private void publish(OutboxEvent event) {

        try {
            ResponseEntity<Void> response = restClient.post()
                    .uri(WEBHOOK_URL)
                    .body(new WebhookEventRequest(
                            event.getId(),
                            event.getEventType(),
                            event.getPayload()
                    ))
                    .retrieve()
                    .toBodilessEntity();

            if (response.getStatusCode().is2xxSuccessful()) {
                event.markProcessed();
                outboxEventRepository.save(event);
            }

        } catch (Exception exception) {
            // Leave event as PENDING.
            // The next scheduled run will retry it.
        }
    }
}