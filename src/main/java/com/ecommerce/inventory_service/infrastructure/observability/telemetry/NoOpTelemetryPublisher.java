package com.ecommerce.inventory_service.infrastructure.observability.telemetry;

import com.ecommerce.inventory_service.domain.events.telemetry.*;

import org.springframework.stereotype.Component;

@Component
public class NoOpTelemetryPublisher
        implements TelemetryPublisher {

    @Override
    public void publish(
            TelemetryEvent telemetryEvent) {

        // No external telemetry yet.
    }
}