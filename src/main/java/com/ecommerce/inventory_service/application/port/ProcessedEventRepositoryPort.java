package com.ecommerce.inventory_service.application.port;

import com.ecommerce.inventory_service.domain.model.ProcessedEvent;
import com.ecommerce.inventory_service.infrastructure.observability.context.ExecutionContext;

/**
 * NOTE:
 * Currently kept inside inventory-service because we are
 * implementing idempotency and retry handling.
 *
 * This may later become a shared infrastructure concern
 * used across multiple services.
 */
public interface ProcessedEventRepositoryPort {

    ProcessedEvent save(
            ExecutionContext context,
            ProcessedEvent processedEvent
    );

    boolean existsByEventId(
            ExecutionContext context,
            String eventId
    );
}