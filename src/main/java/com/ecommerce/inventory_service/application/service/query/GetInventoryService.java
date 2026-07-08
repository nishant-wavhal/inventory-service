package com.ecommerce.inventory_service.application.service.query;

import com.ecommerce.inventory_service.application.port.InventoryRepositoryPort;
import com.ecommerce.inventory_service.domain.exceptions.InventoryNotFoundException;
import com.ecommerce.inventory_service.domain.model.Inventory;

public class GetInventoryService {

    private final InventoryRepositoryPort inventoryRepositoryPort;

    public GetInventoryService(
            InventoryRepositoryPort inventoryRepositoryPort) {

        this.inventoryRepositoryPort =
                inventoryRepositoryPort;
    }

    public Inventory getInventory(
            String sku) {

        return inventoryRepositoryPort
                .findBySku(sku)
                .orElseThrow(
                        () -> new InventoryNotFoundException(
                                "Inventory not found for sku: "
                                        + sku));
    }
}