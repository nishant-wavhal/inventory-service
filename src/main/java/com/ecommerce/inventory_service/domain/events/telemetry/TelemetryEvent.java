package com.ecommerce.inventory_service.domain.events.telemetry;

import com.ecommerce.inventory_service.infrastructure.observability.context.IdentifierType;
import com.ecommerce.inventory_service.infrastructure.observability.outcome.OperationOutcome;

import java.time.Instant;

public class TelemetryEvent {

    private final TelemetryEventType eventType;

    private final IdentifierType identifierType;

    private final String identifier;

    private final String parentIdentifier;

    private final Object payload;

    private final Instant startedAt;

    private final Instant completedAt;

    private final Integer retryCount;

    private final Boolean timedOut;

    private final OperationOutcome outcome;

    public TelemetryEvent(
            TelemetryEventType eventType,
            IdentifierType identifierType,
            String identifier,
            String parentIdentifier,
            Object payload,
            Instant startedAt,
            Instant completedAt,
            Integer retryCount,
            Boolean timedOut,
            OperationOutcome outcome) {

        this.eventType = eventType;
        this.identifierType = identifierType;
        this.identifier = identifier;
        this.parentIdentifier = parentIdentifier;
        this.payload = payload;
        this.startedAt = startedAt;
        this.completedAt = completedAt;
        this.retryCount = retryCount;
        this.timedOut = timedOut;
        this.outcome = outcome;
    }

    public TelemetryEventType getEventType() {
        return eventType;
    }

    public IdentifierType getIdentifierType() {
        return identifierType;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getParentIdentifier() {
        return parentIdentifier;
    }

    public Object getPayload() {
        return payload;
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public Instant getCompletedAt() {
        return completedAt;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public Boolean getTimedOut() {
        return timedOut;
    }

    public OperationOutcome getOutcome() {
        return outcome;
    }
}