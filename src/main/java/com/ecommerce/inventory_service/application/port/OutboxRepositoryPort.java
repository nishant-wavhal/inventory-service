package com.ecommerce.inventory_service.application.port;

import com.ecommerce.inventory_service.domain.model.OutboxEvent;

import java.util.List;

/**
 * NOTE:
 * Currently kept inside inventory-service because we are
 * deeply implementing production-grade reliability concerns.
 *
 * In the future this may be extracted into a shared
 * infrastructure module or saga framework used across:
 *
 * - Order Service
 * - Inventory Service
 * - Payment Service
 *
 * since the transactional outbox pattern is not specific
 * to inventory business logic.
 */
public interface OutboxRepositoryPort {

    OutboxEvent save(OutboxEvent outboxEvent);

    List<OutboxEvent> findPendingEvents(int batchSize );

    OutboxEvent update(OutboxEvent outboxEvent);
}