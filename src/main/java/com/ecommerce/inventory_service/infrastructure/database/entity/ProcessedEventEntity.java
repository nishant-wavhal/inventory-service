package com.ecommerce.inventory_service.infrastructure.database.entity;

import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(
        name = "processed_events",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_processed_event_event_id",
                        columnNames = "event_id"
                )
        }
)
public class ProcessedEventEntity {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    @Column(
            name = "event_id",
            nullable = false,
            unique = true,
            length = 100
    )
    private String eventId;

    @Column(
            name = "event_type",
            nullable = false,
            length = 200
    )
    private String eventType;

    @Column(
            name = "processed_at",
            nullable = false
    )
    private Instant processedAt;

    public ProcessedEventEntity() {
    }

    public ProcessedEventEntity(
            Long id,
            String eventId,
            String eventType,
            Instant processedAt) {

        this.id = id;
        this.eventId = eventId;
        this.eventType = eventType;
        this.processedAt = processedAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(
            String eventId) {

        this.eventId = eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(
            String eventType) {

        this.eventType = eventType;
    }

    public Instant getProcessedAt() {
        return processedAt;
    }

    public void setProcessedAt(
            Instant processedAt) {

        this.processedAt = processedAt;
    }
}