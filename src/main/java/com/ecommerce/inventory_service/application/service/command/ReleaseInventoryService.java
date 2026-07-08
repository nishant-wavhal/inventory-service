package com.ecommerce.inventory_service.application.service.command;

import com.ecommerce.inventory_service.application.command.ReleaseInventoryCommand;
import com.ecommerce.inventory_service.application.port.InventoryEventPublisherPort;
import com.ecommerce.inventory_service.application.port.InventoryRedisPort;
import com.ecommerce.inventory_service.application.port.InventoryRepositoryPort;
import com.ecommerce.inventory_service.domain.decisions.DecisionResult;
import com.ecommerce.inventory_service.domain.decisions.DecisionStatus;
import com.ecommerce.inventory_service.domain.events.InventoryReleasedEvent;
import com.ecommerce.inventory_service.domain.exceptions.InventoryNotFoundException;
import com.ecommerce.inventory_service.domain.model.Inventory;
import com.ecommerce.inventory_service.domain.model.InventoryHold;

import java.time.Instant;

public class ReleaseInventoryService {

    private final InventoryRepositoryPort inventoryRepositoryPort;
    private final InventoryRedisPort inventoryRedisPort;
    private final InventoryEventPublisherPort inventoryEventPublisherPort;

    public ReleaseInventoryService(
            InventoryRepositoryPort inventoryRepositoryPort,
            InventoryRedisPort inventoryRedisPort,
            InventoryEventPublisherPort inventoryEventPublisherPort) {

        this.inventoryRepositoryPort = inventoryRepositoryPort;
        this.inventoryRedisPort = inventoryRedisPort;
        this.inventoryEventPublisherPort = inventoryEventPublisherPort;
    }

    public DecisionResult releaseInventory(
            ReleaseInventoryCommand command) {

        InventoryHold inventoryHold =
                inventoryRedisPort.findHoldByOrderId(
                                command.getOrderId())
                        .orElse(null);

        if (inventoryHold == null) {

            return new DecisionResult(
                    DecisionStatus.REJECTED,
                    "HOLD_NOT_FOUND",
                    "Inventory hold not found");
        }

        Inventory inventory =
                inventoryRepositoryPort.findBySku(
                                inventoryHold.getSku())
                        .orElseThrow(
                                () -> new InventoryNotFoundException(
                                        "Inventory not found for sku: "
                                                + inventoryHold.getSku()));

        inventory.release(
                inventoryHold.getQuantity());

        inventoryRepositoryPort.save(inventory);

        inventoryRedisPort.deleteHold(
                inventoryHold.getOrderId());

        inventoryEventPublisherPort.publish(
                new InventoryReleasedEvent(
                        inventory.getId(),
                        inventoryHold.getOrderId(),
                        inventory.getSku(),
                        inventoryHold.getQuantity(),
                        Instant.now()
                )
        );

        return new DecisionResult(
                DecisionStatus.APPROVED,
                "INVENTORY_RELEASED",
                "Inventory released successfully");
    }
}