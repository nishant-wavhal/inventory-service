package com.ecommerce.inventory_service.infrastructure.resilience.retry;

import com.ecommerce.inventory_service.infrastructure.observability.context.ExecutionIdentifier;
import com.ecommerce.inventory_service.infrastructure.resilience.ResilienceConfig;

import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@Component
public class RetryExecutor {

    private final ResilienceConfig resilienceConfig;

    public RetryExecutor(
            ResilienceConfig resilienceConfig) {

        this.resilienceConfig =
                resilienceConfig;
    }

    public <T> T execute(
            ExecutionIdentifier identifier,
            Supplier<T> operation) {

        int attempts = 0;

        while (true) {

            try {

                return operation.get();

            } catch (Exception ex) {

                attempts++;

                identifier.incrementRetryCount();

                if (attempts >=
                        resilienceConfig.getMaxRetries()) {

                    throw ex;
                }

                sleep();
            }
        }
    }

    private void sleep() {

        try {

            Thread.sleep(
                    resilienceConfig.getRetryDelayMs()
            );

        } catch (InterruptedException ex) {

            Thread.currentThread()
                    .interrupt();

            throw new RuntimeException(
                    ex
            );
        }
    }
}