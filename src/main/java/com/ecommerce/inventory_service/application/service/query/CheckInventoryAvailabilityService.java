package com.ecommerce.inventory_service.application.service.query;

import com.ecommerce.inventory_service.application.port.InventoryRepositoryPort;
import com.ecommerce.inventory_service.domain.exceptions.InventoryNotFoundException;
import com.ecommerce.inventory_service.domain.model.Inventory;

public class CheckInventoryAvailabilityService {

    private final InventoryRepositoryPort inventoryRepositoryPort;

    public CheckInventoryAvailabilityService(
            InventoryRepositoryPort inventoryRepositoryPort) {

        this.inventoryRepositoryPort =
                inventoryRepositoryPort;
    }

    public boolean isAvailable(
            String sku,
            Integer quantity) {

        Inventory inventory =
                inventoryRepositoryPort
                        .findBySku(sku)
                        .orElseThrow(
                                () -> new InventoryNotFoundException(
                                        "Inventory not found for sku: "
                                                + sku));

        return inventory.getAvailableQuantity()
                >= quantity;
    }
}