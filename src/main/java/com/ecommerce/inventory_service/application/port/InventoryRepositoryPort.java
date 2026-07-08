package com.ecommerce.inventory_service.application.port;

import com.ecommerce.inventory_service.infrastructure.observability.context.ExecutionContext;
import com.ecommerce.inventory_service.domain.model.Inventory;

import java.util.Optional;

public interface InventoryRepositoryPort {

Inventory save(
        ExecutionContext context,
        Inventory inventory
);

Optional<Inventory> findById(
        ExecutionContext context,
        Long id
);

Optional<Inventory> findBySku(
        ExecutionContext context,
        String sku
);
}