package com.ecommerce.inventory_service.infrastructure.messaging.kafka.producer;

import com.ecommerce.inventory_service.application.port.InventoryEventPublisherPort;

import com.ecommerce.inventory_service.domain.events.DomainEvent;
import com.ecommerce.inventory_service.domain.events.InventoryCreatedEvent;
import com.ecommerce.inventory_service.domain.events.InventoryDeductedEvent;
import com.ecommerce.inventory_service.domain.events.InventoryExpiredEvent;
import com.ecommerce.inventory_service.domain.events.InventoryReleasedEvent;
import com.ecommerce.inventory_service.domain.events.InventoryReservedEvent;

import com.ecommerce.inventory_service.infrastructure.messaging.kafka.topics.KafkaTopics;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class InventoryEventProducer
        implements InventoryEventPublisherPort {

    private final KafkaTemplate<String, Object>
            kafkaTemplate;

    public InventoryEventProducer(
            KafkaTemplate<String, Object>
                    kafkaTemplate) {

        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(
            DomainEvent event) {

        kafkaTemplate.send(
                resolveTopic(event),
                event.getEventId(),
                event
        );
    }

    private String resolveTopic(
            DomainEvent event) {

        if (event instanceof InventoryCreatedEvent) {
            return KafkaTopics.INVENTORY_CREATED_EVENT;
        }

        if (event instanceof InventoryReservedEvent) {
            return KafkaTopics.INVENTORY_RESERVED_EVENT;
        }

        if (event instanceof InventoryReleasedEvent) {
            return KafkaTopics.INVENTORY_RELEASED_EVENT;
        }

        if (event instanceof InventoryDeductedEvent) {
            return KafkaTopics.INVENTORY_DEDUCTED_EVENT;
        }

        if (event instanceof InventoryExpiredEvent) {
            return KafkaTopics.INVENTORY_EXPIRED_EVENT;
        }

        throw new IllegalArgumentException(
                "Unsupported event type: "
                        + event.getClass().getSimpleName()
        );
    }
}