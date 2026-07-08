package com.ecommerce.inventory_service.infrastructure.observability.logging;

import com.ecommerce.inventory_service.infrastructure.observability.context.ExecutionIdentifier;
import com.ecommerce.inventory_service.infrastructure.observability.context.IdentifierBuilder;
import com.ecommerce.inventory_service.infrastructure.observability.context.IdentifierMetadata;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class StructuredLogger {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(
                    StructuredLogger.class
            );

    public void log(
            ExecutionIdentifier identifier) {

        IdentifierMetadata metadata =
                IdentifierBuilder.resolve(
                        identifier.getIdentifier()
                );

        String status =
                identifier.getOutcome() == null
                        ? "PENDING"
                        : identifier.getOutcome()
                        .getStatus()
                        .name();

        String failureType =
                identifier.getOutcome() == null
                        ? "NONE"
                        : identifier.getOutcome()
                        .getFailureType()
                        .name();

        String failureReason =
                identifier.getOutcome() == null
                        ? null
                        : identifier.getOutcome()
                        .getFailureReason();

        Long durationMs = null;

        if (identifier.getCompletedAt() != null) {

            durationMs =
                    Duration.between(
                                    identifier.getStartedAt(),
                                    identifier.getCompletedAt()
                            )
                            .toMillis();
        }

        LOGGER.info(
                "executionIdentifier={}, parentIdentifier={}, identifierType={}, category={}, operation={}, status={}, failureType={}, failureReason={}, retryCount={}, timedOut={}, durationMs={}",
                identifier.getIdentifier(),
                identifier.getParentIdentifier(),
                identifier.getType(),
                metadata.getCategory(),
                metadata.getOperation(),
                status,
                failureType,
                failureReason,
                identifier.getRetryCount(),
                identifier.isTimedOut(),
                durationMs
        );
    }
}