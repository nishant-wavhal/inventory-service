package com.ecommerce.inventory_service.application.command;

import java.time.Instant;

public class ReleaseInventoryCommand extends BaseCommand {

    private final String orderId;

    public ReleaseInventoryCommand(
            String correlationId,
            String requestId,
            Instant createdAt,
            String orderId) {

        super(correlationId, requestId, createdAt);

        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }
}