package com.ecommerce.inventory_service.domain.events;

import java.time.Instant;

public interface DomainEvent {

    String getEventId();

    Instant getOccurredAt();
}