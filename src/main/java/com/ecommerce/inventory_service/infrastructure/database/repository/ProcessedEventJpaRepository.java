package com.ecommerce.inventory_service.infrastructure.database.repository;

import com.ecommerce.inventory_service.infrastructure.database.entity.ProcessedEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProcessedEventJpaRepository
        extends JpaRepository<ProcessedEventEntity, Long> {

    Optional<ProcessedEventEntity> findByEventId(
            String eventId);

    boolean existsByEventId(
            String eventId);
}