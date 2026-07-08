package com.ecommerce.inventory_service.infrastructure.observability.metrics;

import com.ecommerce.inventory_service.infrastructure.observability.context.ExecutionIdentifier;
import com.ecommerce.inventory_service.infrastructure.observability.context.IdentifierBuilder;
import com.ecommerce.inventory_service.infrastructure.observability.context.IdentifierMetadata;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;

import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class InventoryMetrics {

    private final MeterRegistry meterRegistry;

    public InventoryMetrics(
            MeterRegistry meterRegistry) {

        this.meterRegistry = meterRegistry;
    }

    public void record(
            ExecutionIdentifier identifier) {

        IdentifierMetadata metadata =
                IdentifierBuilder.resolve(
                        identifier.getIdentifier()
                );

        recordExecutionCount(
                metadata
        );

        recordOutcome(
                identifier,
                metadata
        );

        recordRetries(
                identifier,
                metadata
        );

        recordTimeout(
                identifier,
                metadata
        );

        recordDuration(
                identifier,
                metadata
        );
    }

    private void recordExecutionCount(
            IdentifierMetadata metadata) {

        Counter.builder(
                        "inventory_operation_total")
                .tag(
                        "category",
                        metadata.getCategory())
                .tag(
                        "operation",
                        metadata.getOperation())
                .register(meterRegistry)
                .increment();
    }

    private void recordOutcome(
            ExecutionIdentifier identifier,
            IdentifierMetadata metadata) {

        if (identifier.getOutcome() == null) {
            return;
        }

        Counter.builder(
                        "inventory_operation_outcome")
                .tag(
                        "category",
                        metadata.getCategory())
                .tag(
                        "operation",
                        metadata.getOperation())
                .tag(
                        "status",
                        identifier.getOutcome()
                                .getStatus()
                                .name())
                .register(meterRegistry)
                .increment();
    }

    private void recordRetries(
            ExecutionIdentifier identifier,
            IdentifierMetadata metadata) {

        if (identifier.getRetryCount() <= 0) {
            return;
        }

        Counter.builder(
                        "inventory_operation_retry")
                .tag(
                        "category",
                        metadata.getCategory())
                .tag(
                        "operation",
                        metadata.getOperation())
                .register(meterRegistry)
                .increment(
                        identifier.getRetryCount()
                );
    }

    private void recordTimeout(
            ExecutionIdentifier identifier,
            IdentifierMetadata metadata) {

        if (!identifier.isTimedOut()) {
            return;
        }

        Counter.builder(
                        "inventory_operation_timeout")
                .tag(
                        "category",
                        metadata.getCategory())
                .tag(
                        "operation",
                        metadata.getOperation())
                .register(meterRegistry)
                .increment();
    }

    private void recordDuration(
            ExecutionIdentifier identifier,
            IdentifierMetadata metadata) {

        if (identifier.getCompletedAt() == null) {
            return;
        }

        Duration duration =
                Duration.between(
                        identifier.getStartedAt(),
                        identifier.getCompletedAt()
                );

        Timer.builder(
                        "inventory_operation_duration")
                .tag(
                        "category",
                        metadata.getCategory())
                .tag(
                        "operation",
                        metadata.getOperation())
                .register(meterRegistry)
                .record(duration);
    }
}