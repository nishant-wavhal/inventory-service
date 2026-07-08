package com.ecommerce.inventory_service.application.service.command;

import com.ecommerce.inventory_service.application.command.ReserveInventoryCommand;
import com.ecommerce.inventory_service.application.port.InventoryEventPublisherPort;
import com.ecommerce.inventory_service.application.port.InventoryRedisPort;
import com.ecommerce.inventory_service.application.port.InventoryRepositoryPort;
import com.ecommerce.inventory_service.domain.decisions.DecisionResult;
import com.ecommerce.inventory_service.domain.decisions.DecisionStatus;
import com.ecommerce.inventory_service.domain.events.InventoryReservedEvent;
import com.ecommerce.inventory_service.domain.exceptions.InventoryNotFoundException;
import com.ecommerce.inventory_service.domain.model.Inventory;
import com.ecommerce.inventory_service.domain.model.InventoryHold;

import java.time.Instant;

public class ReserveInventoryService {

    private final InventoryRepositoryPort inventoryRepositoryPort;
    private final InventoryRedisPort inventoryRedisPort;
    private final InventoryEventPublisherPort inventoryEventPublisherPort;

    public ReserveInventoryService(
            InventoryRepositoryPort inventoryRepositoryPort,
            InventoryRedisPort inventoryRedisPort,
            InventoryEventPublisherPort inventoryEventPublisherPort) {

        this.inventoryRepositoryPort = inventoryRepositoryPort;
        this.inventoryRedisPort = inventoryRedisPort;
        this.inventoryEventPublisherPort = inventoryEventPublisherPort;
    }

    public DecisionResult reserveInventory(
            ReserveInventoryCommand command) {

        Inventory inventory =
                inventoryRepositoryPort.findBySku(
                                command.getSku())
                        .orElseThrow(
                                () -> new InventoryNotFoundException(
                                        "Inventory not found for sku: "
                                                + command.getSku()));

        inventory.reserve(command.getQuantity());

        inventoryRepositoryPort.save(inventory);

        InventoryHold inventoryHold =
                new InventoryHold(
                        null,
                        command.getOrderId(),
                        command.getSku(),
                        command.getQuantity(),
                        Instant.now().plusSeconds(900)
                );

        inventoryRedisPort.saveHold(inventoryHold);

        inventoryEventPublisherPort.publish(
                new InventoryReservedEvent(
                        inventory.getId(),
                        command.getOrderId(),
                        inventory.getSku(),
                        command.getQuantity(),
                        Instant.now()
                )
        );

        return new DecisionResult(
                DecisionStatus.APPROVED,
                "INVENTORY_RESERVED",
                "Inventory reserved successfully");
    }
}