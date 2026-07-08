package com.ecommerce.inventory_service.infrastructure.resilience.timeout;

import com.ecommerce.inventory_service.infrastructure.observability.context.ExecutionIdentifier;

import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Component
public class TimeoutExecutor {

    public <T> T execute(
            ExecutionIdentifier identifier,
            Supplier<T> operation,
            long timeoutMs) {

        try {

            return CompletableFuture
                    .supplyAsync(operation)
                    .get(
                            timeoutMs,
                            TimeUnit.MILLISECONDS
                    );

        } catch (Exception ex) {

            identifier.markTimedOut();

            throw new RuntimeException(ex);
        }
    }
}