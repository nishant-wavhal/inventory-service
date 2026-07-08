package com.ecommerce.inventory_service.infrastructure.messaging.kafka.dlq;


import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class DeadLetterPublisher {

    private final KafkaTemplate<String, Object>
            kafkaTemplate;

    public DeadLetterPublisher(
            KafkaTemplate<String, Object>
                    kafkaTemplate) {

        this.kafkaTemplate = kafkaTemplate;
    }

    public void publish(
            String dlqTopic,
            String key,
            Object message) {

        kafkaTemplate.send(
                dlqTopic,
                key,
                message
        );
    }
}