package com.sivasaran.ledger.webhook;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReceivedEventRepository
        extends JpaRepository<ReceivedEvent, UUID> {
}