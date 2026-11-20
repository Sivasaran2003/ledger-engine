package com.sivasaran.ledger.webhook;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "received_events")
public class ReceivedEvent {

    @Id
    private UUID eventId;

    @Column(nullable = false)
    private String eventType;

    @Column(nullable = false)
    private String payload;

    @Column(nullable = false)
    private OffsetDateTime receivedAt;

    protected ReceivedEvent() {
    }

    public ReceivedEvent(
            UUID eventId,
            String eventType,
            String payload) {

        this.eventId = eventId;
        this.eventType = eventType;
        this.payload = payload;
        this.receivedAt = OffsetDateTime.now();
    }
}