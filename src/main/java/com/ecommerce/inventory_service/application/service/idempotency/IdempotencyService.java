package com.ecommerce.inventory_service.application.service.idempotency;

import com.ecommerce.inventory_service.application.port.ProcessedEventRepositoryPort;
import com.ecommerce.inventory_service.domain.model.ProcessedEvent;

import java.time.Instant;

public class IdempotencyService {

    private final ProcessedEventRepositoryPort
            processedEventRepositoryPort;

    public IdempotencyService(
            ProcessedEventRepositoryPort
                    processedEventRepositoryPort) {

        this.processedEventRepositoryPort =
                processedEventRepositoryPort;
    }

    public boolean isAlreadyProcessed(
            String eventId) {

        return processedEventRepositoryPort
                .existsByEventId(eventId);
    }

    public void markProcessed(
            String eventId,
            String eventType) {

        ProcessedEvent processedEvent =
                new ProcessedEvent(
                        null,
                        eventId,
                        eventType,
                        Instant.now()
                );

        processedEventRepositoryPort
                .save(processedEvent);
    }
}