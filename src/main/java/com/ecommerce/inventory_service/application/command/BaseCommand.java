package com.ecommerce.inventory_service.application.command;

import java.time.Instant;

public abstract class BaseCommand {

    private final String correlationId;
    private final String requestId;
    private final Instant createdAt;

    protected BaseCommand(
            String correlationId,
            String requestId,
            Instant createdAt) {

        this.correlationId = correlationId;
        this.requestId = requestId;
        this.createdAt = createdAt;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public String getRequestId() {
        return requestId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}