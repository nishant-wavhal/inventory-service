package com.ecommerce.inventory_service.infrastructure.observability.telemetry;

import com.ecommerce.inventory_service.domain.events.telemetry.*;

public interface TelemetryPublisher {

    void publish(
            TelemetryEvent telemetryEvent
    );
}