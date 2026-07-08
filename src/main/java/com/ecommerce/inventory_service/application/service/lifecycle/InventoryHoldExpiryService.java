package com.ecommerce.inventory_service.application.service.lifecycle;

import com.ecommerce.inventory_service.application.port.InventoryEventPublisherPort;
import com.ecommerce.inventory_service.application.port.InventoryRedisPort;
import com.ecommerce.inventory_service.application.port.InventoryRepositoryPort;
import com.ecommerce.inventory_service.domain.events.InventoryExpiredEvent;
import com.ecommerce.inventory_service.domain.model.Inventory;
import com.ecommerce.inventory_service.domain.model.InventoryHold;

import java.time.Instant;
import java.util.List;

public class InventoryHoldExpiryService {

    private final InventoryRepositoryPort inventoryRepositoryPort;
    private final InventoryRedisPort inventoryRedisPort;
    private final InventoryEventPublisherPort inventoryEventPublisherPort;

    public InventoryHoldExpiryService(
            InventoryRepositoryPort inventoryRepositoryPort,
            InventoryRedisPort inventoryRedisPort,
            InventoryEventPublisherPort inventoryEventPublisherPort) {

        this.inventoryRepositoryPort = inventoryRepositoryPort;
        this.inventoryRedisPort = inventoryRedisPort;
        this.inventoryEventPublisherPort = inventoryEventPublisherPort;
    }

    public void processExpiredHolds() {

        List<InventoryHold> expiredHolds =
                inventoryRedisPort.findExpiredHolds();

        for (InventoryHold inventoryHold : expiredHolds) {

            Inventory inventory =
                    inventoryRepositoryPort
                            .findBySku(inventoryHold.getSku())
                            .orElse(null);

            if (inventory == null) {
                continue;
            }

            inventory.release(
                    inventoryHold.getQuantity());

            inventoryRepositoryPort.save(
                    inventory);

            inventoryRedisPort.deleteHold(
                    inventoryHold.getOrderId());

            Instant occurredAt = Instant.now();

            inventoryEventPublisherPort.publish(
                    new InventoryExpiredEvent(
                            inventory.getId(),
                            inventoryHold.getOrderId(),
                            inventory.getSku(),
                            inventoryHold.getQuantity(),
                            occurredAt
                    )
            );
        }
    }
}