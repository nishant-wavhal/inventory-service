/*
 * RetryTopicHandler
 *
 * Purpose
 * -------
 * Central place for retry policy decisions.
 *
 * Spring Kafka currently handles:
 * - Retry Topics
 * - Backoff
 * - Retry Scheduling
 * - Dead Letter Routing
 *
 * Current MVP Tradeoff
 * --------------------
 * We rely completely on Spring Kafka retry
 * capabilities and do not implement custom
 * retry behavior.
 *
 * Future Responsibilities
 * -----------------------
 * 1. Failure Classification
 *
 *    Retry:
 *    - RedisConnectionException
 *    - DatabaseConnectionException
 *    - SocketTimeoutException
 *
 *    No Retry:
 *    - InventoryNotFoundException
 *    - DuplicateSkuException
 *    - InvalidInventoryOperationException
 *
 * 2. Retry Policy Management
 *
 *    - Fixed Backoff
 *    - Exponential Backoff
 *    - Custom Retry Counts
 *
 * 3. Topic Strategy
 *
 *    reserve.command.retry
 *    release.command.retry
 *    deduct.command.retry
 *
 * 4. Retry Observability
 *
 *    - Retry Metrics
 *    - Retry Counters
 *    - Retry Dashboards
 *
 * 5. Operational Controls
 *
 *    - Pause Retries
 *    - Replay Retries
 *    - Manual Retry Trigger
 *
 * Architectural Intent
 * --------------------
 * Spring Kafka handles HOW retries occur.
 *
 * RetryTopicHandler will eventually decide:
 * - WHEN retries occur
 * - WHY retries occur
 * - WHICH exceptions should retry
 */

package com.ecommerce.inventory_service.infrastructure.messaging.kafka.retry;
import com.ecommerce.inventory_service.domain.exceptions.DuplicateSkuException;
import com.ecommerce.inventory_service.domain.exceptions.InventoryNotAvailableException;
import com.ecommerce.inventory_service.domain.exceptions.InventoryNotFoundException;
import com.ecommerce.inventory_service.domain.exceptions.InvalidInventoryOperationException;

import com.ecommerce.inventory_service.infrastructure.messaging.kafka.topics.KafkaTopics;

import org.springframework.stereotype.Component;

@Component
public class RetryTopicHandler {

    public boolean shouldRetry(
            Exception exception) {

        return !(exception instanceof InventoryNotFoundException)
                && !(exception instanceof DuplicateSkuException)
                && !(exception instanceof InventoryNotAvailableException)
                && !(exception instanceof InvalidInventoryOperationException);
    }

    public String getRetryTopic(
            String sourceTopic) {

        switch (sourceTopic) {

            case KafkaTopics.CREATE_INVENTORY_COMMAND:
                return KafkaTopics.CREATE_INVENTORY_RETRY;

            case KafkaTopics.RESERVE_INVENTORY_COMMAND:
                return KafkaTopics.RESERVE_INVENTORY_RETRY;

            case KafkaTopics.RELEASE_INVENTORY_COMMAND:
                return KafkaTopics.RELEASE_INVENTORY_RETRY;

            case KafkaTopics.DEDUCT_INVENTORY_COMMAND:
                return KafkaTopics.DEDUCT_INVENTORY_RETRY;

            default:
                throw new IllegalArgumentException(
                        "Unsupported topic: " + sourceTopic);
        }
    }

    public String getDlqTopic(
            String sourceTopic) {

        switch (sourceTopic) {

            case KafkaTopics.CREATE_INVENTORY_COMMAND:
                return KafkaTopics.CREATE_INVENTORY_DLQ;

            case KafkaTopics.RESERVE_INVENTORY_COMMAND:
                return KafkaTopics.RESERVE_INVENTORY_DLQ;

            case KafkaTopics.RELEASE_INVENTORY_COMMAND:
                return KafkaTopics.RELEASE_INVENTORY_DLQ;

            case KafkaTopics.DEDUCT_INVENTORY_COMMAND:
                return KafkaTopics.DEDUCT_INVENTORY_DLQ;

            default:
                throw new IllegalArgumentException(
                        "Unsupported topic: " + sourceTopic);
        }
    }
}