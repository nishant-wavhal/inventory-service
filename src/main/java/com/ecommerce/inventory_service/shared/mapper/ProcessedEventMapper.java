package com.ecommerce.inventory_service.shared.mapper;

import com.ecommerce.inventory_service.domain.model.ProcessedEvent;
import com.ecommerce.inventory_service.infrastructure.database.entity.ProcessedEventEntity;

public final class ProcessedEventMapper {

    private ProcessedEventMapper() {
    }

    public static ProcessedEvent toDomain(
            ProcessedEventEntity entity) {

        if (entity == null) {
            return null;
        }

        return new ProcessedEvent(
                entity.getId(),
                entity.getEventId(),
                entity.getEventType(),
                entity.getProcessedAt()
        );
    }

    public static ProcessedEventEntity toEntity(
            ProcessedEvent domain) {

        if (domain == null) {
            return null;
        }

        return new ProcessedEventEntity(
                domain.getId(),
                domain.getEventId(),
                domain.getEventType(),
                domain.getProcessedAt()
        );
    }
}