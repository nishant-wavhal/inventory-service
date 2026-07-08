package com.ecommerce.inventory_service.domain.model;

import java.time.Instant;

public class ProcessedEvent {

    private Long id;
    private String eventId;
    private String eventType;
    private Instant processedAt;

    public ProcessedEvent() {
    }

    public ProcessedEvent(
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

    public String getEventId() {
        return eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public Instant getProcessedAt() {
        return processedAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setProcessedAt(Instant processedAt) {
        this.processedAt = processedAt;
    }
}