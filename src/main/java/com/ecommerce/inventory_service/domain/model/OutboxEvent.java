package com.ecommerce.inventory_service.domain.model;

import com.ecommerce.inventory_service.domain.enums.OutboxStatus;

import java.time.Instant;

public class OutboxEvent {

    private Long id;

    private String eventType;

    private String payload;

    private OutboxStatus status;

    private Instant createdAt;

    public OutboxEvent() {
    }

    public OutboxEvent(
            Long id,
            String eventType,
            String payload,
            OutboxStatus status,
            Instant createdAt) {

        this.id = id;
        this.eventType = eventType;
        this.payload = payload;
        this.status = status;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public String getEventType() {
        return eventType;
    }

    public String getPayload() {
        return payload;
    }

    public OutboxStatus getStatus() {
        return status;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public void setStatus(OutboxStatus status) {
        this.status = status;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }
}