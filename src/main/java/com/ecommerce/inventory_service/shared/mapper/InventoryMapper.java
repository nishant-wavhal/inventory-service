package com.ecommerce.inventory_service.shared.mapper;

import com.ecommerce.inventory_service.domain.model.Inventory;
import com.ecommerce.inventory_service.infrastructure.database.entity.InventoryEntity;

public final class InventoryMapper {

    private InventoryMapper() {
    }

    public static Inventory toDomain(
            InventoryEntity entity) {

        if (entity == null) {
            return null;
        }

        return new Inventory(
                entity.getId(),
                entity.getSku(),
                entity.getAvailableQuantity(),
                entity.getReservedQuantity(),
                entity.getStatus(),
                entity.getVersion()
        );
    }

    public static InventoryEntity toEntity(
            Inventory inventory) {

        if (inventory == null) {
            return null;
        }

        InventoryEntity entity =
                new InventoryEntity();

        entity.setId(
                inventory.getId());

        entity.setSku(
                inventory.getSku());

        entity.setAvailableQuantity(
                inventory.getAvailableQuantity());

        entity.setReservedQuantity(
                inventory.getReservedQuantity());

        entity.setStatus(
                inventory.getStatus());

        entity.setVersion(
                inventory.getVersion());

        return entity;
    }
}