package com.sivasaran.ledger.outbox;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "outbox_events")
public class OutboxEvent {

    @Id
    private UUID id;

    @Column(name = "event_type", nullable = false)
    private String eventType;

    @Column(name = "aggregate_id", nullable = false)
    private UUID aggregateId;

    @Column(nullable = false)
    private String payload;

    @Column(nullable = false)
    private String status;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    @Column(name = "processed_at")
    private OffsetDateTime processedAt;

    protected OutboxEvent() {
    }

    public OutboxEvent(
            String eventType,
            UUID aggregateId,
            String payload) {

        this.id = UUID.randomUUID();
        this.eventType = eventType;
        this.aggregateId = aggregateId;
        this.payload = payload;
        this.status = "PENDING";
        this.createdAt = OffsetDateTime.now();
    }

    public UUID getId() {
        return id;
    }

    public String getEventType() {
        return eventType;
    }

    public UUID getAggregateId() {
        return aggregateId;
    }

    public String getPayload() {
        return payload;
    }

    public String getStatus() {
        return status;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public OffsetDateTime getProcessedAt() {
        return processedAt;
    }

    public void markProcessed() {
        this.status = "PUBLISHED";
        this.processedAt = OffsetDateTime.now();
    }
}