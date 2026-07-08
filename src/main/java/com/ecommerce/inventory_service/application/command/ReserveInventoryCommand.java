package com.ecommerce.inventory_service.application.command;

import java.time.Instant;

public class ReserveInventoryCommand extends BaseCommand {

    private final String orderId;
    private final String sku;
    private final Integer quantity;

    public ReserveInventoryCommand(
            String correlationId,
            String requestId,
            Instant createdAt,
            String orderId,
            String sku,
            Integer quantity) {

        super(correlationId, requestId, createdAt);

        this.orderId = orderId;
        this.sku = sku;
        this.quantity = quantity;
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
}