package com.ecommerce.inventory_service.application.command;

import java.time.Instant;

public class CreateInventoryCommand extends BaseCommand {

    private final String sku;
    private final Integer quantity;

    public CreateInventoryCommand(
            String correlationId,
            String requestId,
            Instant createdAt,
            String sku,
            Integer quantity) {

        super(correlationId, requestId, createdAt);

        this.sku = sku;
        this.quantity = quantity;
    }

    public String getSku() {
        return sku;
    }

    public Integer getQuantity() {
        return quantity;
    }
}