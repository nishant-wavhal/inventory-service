package com.ecommerce.inventory_service.domain.events;

import java.time.Instant;

public class InventoryReservedEvent implements DomainEvent {

    private final Long inventoryId;
    private final String orderId;
    private final String sku;
    private final Integer quantity;
    private final Instant occurredAt;
    private String eventId;

    public InventoryReservedEvent(
            Long inventoryId,
            String orderId,
            String sku,
            Integer quantity,
            Instant occurredAt) {

        this.inventoryId = inventoryId;
        this.orderId = orderId;
        this.sku = sku;
        this.quantity = quantity;
        this.occurredAt = occurredAt;
    }

    public Long getInventoryId() {
        return inventoryId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getSku() {
        return sku;
    }

    public Integer getQuantity() {
        return quantity;
    }
    @Override
    public Instant getOccurredAt() {
        return occurredAt;
    }

    
    @Override
    public String getEventId() {
        return eventId;
    }
}