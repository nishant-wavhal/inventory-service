package com.ecommerce.inventory_service.shared.mapper;

import com.ecommerce.inventory_service.infrastructure.observability.context.ExecutionIdentifier;
import com.ecommerce.inventory_service.domain.events.telemetry.TelemetryEvent;
import com.ecommerce.inventory_service.domain.events.telemetry.TelemetryEventType;

public final class TelemetryMapper {

    private TelemetryMapper() {
    }

    public static TelemetryEvent toStartEvent(
            ExecutionIdentifier identifier) {

        return new TelemetryEvent(
                TelemetryEventType.START,
                identifier.getType(),
                identifier.getIdentifier(),
                identifier.getParentIdentifier(),
                identifier.getPayload(),
                identifier.getStartedAt(),
                null,
                identifier.getRetryCount(),
                identifier.isTimedOut(),
                identifier.getOutcome()
        );
    }

    public static TelemetryEvent toEndEvent(
            ExecutionIdentifier identifier) {

        return new TelemetryEvent(
                TelemetryEventType.END,
                identifier.getType(),
                identifier.getIdentifier(),
                identifier.getParentIdentifier(),
                identifier.getPayload(),
                identifier.getStartedAt(),
                identifier.getCompletedAt(),
                identifier.getRetryCount(),
                identifier.isTimedOut(),
                identifier.getOutcome()
        );
    }
}