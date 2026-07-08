package com.ecommerce.inventory_service.application.service.outbox;

import com.ecommerce.inventory_service.application.port.OutboxRepositoryPort;
import com.ecommerce.inventory_service.domain.enums.OutboxStatus;
import com.ecommerce.inventory_service.domain.model.OutboxEvent;

import java.time.Instant;
import java.util.List;

public class OutboxPublisherService {

    private final OutboxRepositoryPort outboxRepositoryPort;

    public OutboxPublisherService(
            OutboxRepositoryPort outboxRepositoryPort) {

        this.outboxRepositoryPort = outboxRepositoryPort;
    }

    public OutboxEvent createOutboxEvent(
            String eventType,
            String payload) {

        OutboxEvent outboxEvent =
                new OutboxEvent(
                        null,
                        eventType,
                        payload,
                        OutboxStatus.PENDING,
                        Instant.now()
                );

        return outboxRepositoryPort.save(
                outboxEvent);
    }

    public List<OutboxEvent> getPendingEvents(
            int batchSize) {

        return outboxRepositoryPort
                .findPendingEvents(batchSize);
    }

    public OutboxEvent markPublished(
            OutboxEvent outboxEvent) {

        outboxEvent.setStatus(
                OutboxStatus.PUBLISHED);

        return outboxRepositoryPort
                .update(outboxEvent);
    }
}