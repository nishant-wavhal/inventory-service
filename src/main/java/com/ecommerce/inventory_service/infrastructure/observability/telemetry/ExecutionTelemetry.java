package com.ecommerce.inventory_service.infrastructure.observability.telemetry;

import com.ecommerce.inventory_service.infrastructure.observability.context.ExecutionIdentifier;
import com.ecommerce.inventory_service.infrastructure.observability.logging.StructuredLogger;
import com.ecommerce.inventory_service.infrastructure.observability.metrics.InventoryMetrics;
import com.ecommerce.inventory_service.shared.mapper.*;

import org.springframework.stereotype.Component;

@Component
public class ExecutionTelemetry {

    private final TelemetryPublisher telemetryPublisher;

    private final InventoryMetrics inventoryMetrics;

    private final StructuredLogger structuredLogger;

    public ExecutionTelemetry(
            TelemetryPublisher telemetryPublisher,
            InventoryMetrics inventoryMetrics,
            StructuredLogger structuredLogger) {

        this.telemetryPublisher =
                telemetryPublisher;

        this.inventoryMetrics =
                inventoryMetrics;

        this.structuredLogger =
                structuredLogger;
    }

    public void start(
            ExecutionIdentifier identifier) {

        telemetryPublisher.publish(
                TelemetryMapper.toStartEvent(
                        identifier
                )
        );

        inventoryMetrics.record(
                identifier
        );

        structuredLogger.log(
                identifier
        );
    }

    public void end(
            ExecutionIdentifier identifier) {

        telemetryPublisher.publish(
                TelemetryMapper.toEndEvent(
                        identifier
                )
        );

        inventoryMetrics.record(
                identifier
        );

        structuredLogger.log(
                identifier
        );
    }
}