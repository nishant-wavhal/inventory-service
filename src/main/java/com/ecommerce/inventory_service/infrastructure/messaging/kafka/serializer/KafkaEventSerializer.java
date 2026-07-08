package com.ecommerce.inventory_service.infrastructure.messaging.kafka.serializer;

/*
 * Future Responsibilities
 *
 * - Avro Serialization
 * - Protobuf Serialization
 * - Schema Registry Integration
 * - Message Compression
 * - Message Encryption
 * - Custom Binary Protocols
 * - Backward Compatibility Strategies
 *
 * Current Tradeoff
 *
 * Spring Kafka JsonSerializer and
 * JsonDeserializer are sufficient
 * for the MVP.
 *
 * Serialization configuration is
 * handled through KafkaConfig.
 */
public final class KafkaEventSerializer {

    private KafkaEventSerializer() {
    }
}