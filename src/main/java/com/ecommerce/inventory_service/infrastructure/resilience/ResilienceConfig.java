package com.ecommerce.inventory_service.infrastructure.resilience;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ResilienceConfig {

    @Value("${resilience.retry.max-attempts:3}")
    private int maxRetries;

    @Value("${resilience.retry.delay-ms:1000}")
    private long retryDelayMs;

    @Value("${resilience.timeout.database-ms:3000}")
    private long databaseTimeoutMs;

    @Value("${resilience.timeout.redis-ms:1000}")
    private long redisTimeoutMs;

    @Value("${resilience.timeout.kafka-ms:5000}")
    private long kafkaTimeoutMs;

    public int getMaxRetries() {
        return maxRetries;
    }

    public long getRetryDelayMs() {
        return retryDelayMs;
    }

    public long getDatabaseTimeoutMs() {
        return databaseTimeoutMs;
    }

    public long getRedisTimeoutMs() {
        return redisTimeoutMs;
    }

    public long getKafkaTimeoutMs() {
        return kafkaTimeoutMs;
    }
}