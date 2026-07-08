package com.ecommerce.inventory_service.infrastructure.messaging.kafka.topics;

public final class KafkaTopics {

    private KafkaTopics() {
    }

    // Commands

    public static final String CREATE_INVENTORY_COMMAND =
            "inventory.create.command";

    public static final String RESERVE_INVENTORY_COMMAND =
            "inventory.reserve.command";

    public static final String RELEASE_INVENTORY_COMMAND =
            "inventory.release.command";

    public static final String DEDUCT_INVENTORY_COMMAND =
            "inventory.deduct.command";

    // Events

    public static final String INVENTORY_CREATED_EVENT =
            "inventory.created.event";

    public static final String INVENTORY_RESERVED_EVENT =
            "inventory.reserved.event";

    public static final String INVENTORY_RELEASED_EVENT =
            "inventory.released.event";

    public static final String INVENTORY_DEDUCTED_EVENT =
            "inventory.deducted.event";

    public static final String INVENTORY_EXPIRED_EVENT =
            "inventory.expired.event";

    // Retry

    public static final String CREATE_INVENTORY_RETRY =
            "inventory.create.retry";

    public static final String RESERVE_INVENTORY_RETRY =
            "inventory.reserve.retry";

    public static final String RELEASE_INVENTORY_RETRY =
            "inventory.release.retry";

    public static final String DEDUCT_INVENTORY_RETRY =
            "inventory.deduct.retry";

    // DLQ

    public static final String CREATE_INVENTORY_DLQ =
            "inventory.create.dlq";

    public static final String RESERVE_INVENTORY_DLQ =
            "inventory.reserve.dlq";

    public static final String RELEASE_INVENTORY_DLQ =
            "inventory.release.dlq";

    public static final String DEDUCT_INVENTORY_DLQ =
            "inventory.deduct.dlq";
}