package com.ecommerce.inventory_service.application.service.command;

import com.ecommerce.inventory_service.application.command.DeductInventoryCommand;
import com.ecommerce.inventory_service.application.port.InventoryEventPublisherPort;
import com.ecommerce.inventory_service.application.port.InventoryRedisPort;
import com.ecommerce.inventory_service.application.port.InventoryRepositoryPort;
import com.ecommerce.inventory_service.domain.decisions.DecisionResult;
import com.ecommerce.inventory_service.domain.decisions.DecisionStatus;
import com.ecommerce.inventory_service.domain.events.InventoryDeductedEvent;
import com.ecommerce.inventory_service.domain.exceptions.InventoryNotFoundException;
import com.ecommerce.inventory_service.domain.model.Inventory;
import com.ecommerce.inventory_service.domain.model.InventoryHold;

import java.time.Instant;

public class DeductInventoryService {

    private final InventoryRepositoryPort inventoryRepositoryPort;
    private final InventoryRedisPort inventoryRedisPort;
    private final InventoryEventPublisherPort inventoryEventPublisherPort;

    public DeductInventoryService(
            InventoryRepositoryPort inventoryRepositoryPort,
            InventoryRedisPort inventoryRedisPort,
            InventoryEventPublisherPort inventoryEventPublisherPort) {

        this.inventoryRepositoryPort = inventoryRepositoryPort;
        this.inventoryRedisPort = inventoryRedisPort;
        this.inventoryEventPublisherPort = inventoryEventPublisherPort;
    }

    public DecisionResult deductInventory(
            DeductInventoryCommand command) {

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

        inventory.deduct(
                inventoryHold.getQuantity());

        inventoryRepositoryPort.save(inventory);

        inventoryRedisPort.deleteHold(
                inventoryHold.getOrderId());

        inventoryEventPublisherPort.publish(
                new InventoryDeductedEvent(
                        inventory.getId(),
                        inventoryHold.getOrderId(),
                        inventory.getSku(),
                        inventoryHold.getQuantity(),
                        Instant.now()
                )
        );

        return new DecisionResult(
                DecisionStatus.APPROVED,
                "INVENTORY_DEDUCTED",
                "Inventory deducted successfully");
    }
}