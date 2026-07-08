package com.ecommerce.inventory_service.application.port;

import com.ecommerce.inventory_service.domain.events.DomainEvent;

public interface InventoryEventPublisherPort {

    void publish(DomainEvent event);
}