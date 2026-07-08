package com.ecommerce.inventory_service.infrastructure.database.repository;

import com.ecommerce.inventory_service.infrastructure.database.entity.InventoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InventoryJpaRepository
        extends JpaRepository<InventoryEntity, Long> {

    Optional<InventoryEntity> findBySku(
            String sku);
}