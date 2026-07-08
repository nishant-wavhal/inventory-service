package com.ecommerce.inventory_service.infrastructure.observability.context;

import com.ecommerce.inventory_service.infrastructure.observability.outcome.OperationOutcome;

import java.time.Instant;

public class ExecutionIdentifier {

    private final IdentifierType type;

    private final String identifier;

    private final String parentIdentifier;

    private final Object payload;

    private final Instant startedAt;

    private Instant completedAt;

    private int retryCount;

    private boolean timedOut;

    private OperationOutcome outcome;

    public ExecutionIdentifier(
            IdentifierType type,
            String identifier,
            String parentIdentifier,
            Object payload) {

        this.type = type;
        this.identifier = identifier;
        this.parentIdentifier = parentIdentifier;
        this.payload = payload;
        this.startedAt = Instant.now();
    }

    public IdentifierType getType() {
        return type;
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

    public int getRetryCount() {
        return retryCount;
    }

    public boolean isTimedOut() {
        return timedOut;
    }

    public OperationOutcome getOutcome() {
        return outcome;
    }

    public void setOutcome(
            OperationOutcome outcome) {

        this.outcome = outcome;
    }

    public void markCompleted() {
        this.completedAt = Instant.now();
    }

    public void incrementRetryCount() {
        this.retryCount++;
    }

    public void markTimedOut() {
        this.timedOut = true;
    }
}