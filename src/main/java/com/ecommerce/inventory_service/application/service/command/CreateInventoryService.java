package com.ecommerce.inventory_service.application.service.command;

import com.ecommerce.inventory_service.application.command.CreateInventoryCommand;
import com.ecommerce.inventory_service.application.port.InventoryEventPublisherPort;
import com.ecommerce.inventory_service.application.port.InventoryRepositoryPort;
import com.ecommerce.inventory_service.domain.decisions.DecisionResult;
import com.ecommerce.inventory_service.domain.decisions.DecisionStatus;
import com.ecommerce.inventory_service.domain.enums.InventoryStatus;
import com.ecommerce.inventory_service.domain.events.InventoryCreatedEvent;
import com.ecommerce.inventory_service.domain.model.Inventory;

import java.time.Instant;

public class CreateInventoryService {

    private final InventoryRepositoryPort inventoryRepositoryPort;
    private final InventoryEventPublisherPort inventoryEventPublisherPort;

    public CreateInventoryService(
            InventoryRepositoryPort inventoryRepositoryPort,
            InventoryEventPublisherPort inventoryEventPublisherPort) {

        this.inventoryRepositoryPort = inventoryRepositoryPort;
        this.inventoryEventPublisherPort = inventoryEventPublisherPort;
    }

    public DecisionResult createInventory(
            CreateInventoryCommand command) {

        if (inventoryRepositoryPort.findBySku(command.getSku()).isPresent()) {

            return new DecisionResult(
                    DecisionStatus.REJECTED,
                    "SKU_ALREADY_EXISTS",
                    "Inventory already exists for sku: "
                            + command.getSku());
        }

        Inventory inventory = new Inventory(
                null,
                command.getSku(),
                command.getQuantity(),
                0,
                InventoryStatus.ACTIVE,
                0L
        );

        Inventory savedInventory =
                inventoryRepositoryPort.save(inventory);

        inventoryEventPublisherPort.publish(
                new InventoryCreatedEvent(
                        savedInventory.getId(),
                        savedInventory.getSku(),
                        savedInventory.getAvailableQuantity(),
                        Instant.now()
                )
        );

        return new DecisionResult(
                DecisionStatus.APPROVED,
                "INVENTORY_CREATED",
                "Inventory created successfully");
    }
}