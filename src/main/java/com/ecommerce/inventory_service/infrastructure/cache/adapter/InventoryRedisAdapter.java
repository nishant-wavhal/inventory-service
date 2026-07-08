package com.ecommerce.inventory_service.infrastructure.cache.adapter;

import com.ecommerce.inventory_service.application.port.InventoryRedisPort;
import com.ecommerce.inventory_service.domain.model.InventoryHold;
import com.ecommerce.inventory_service.infrastructure.cache.repository.InventoryHoldRedisRepository;
import com.ecommerce.inventory_service.infrastructure.observability.context.*;
import com.ecommerce.inventory_service.infrastructure.observability.logging.StructuredLogger;
import com.ecommerce.inventory_service.infrastructure.observability.metrics.InventoryMetrics;
import com.ecommerce.inventory_service.infrastructure.observability.outcome.*;
import com.ecommerce.inventory_service.infrastructure.resilience.ResilienceConfig;
import com.ecommerce.inventory_service.infrastructure.resilience.retry.RetryExecutor;
import com.ecommerce.inventory_service.infrastructure.resilience.timeout.TimeoutExecutor;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class InventoryRedisAdapter
        implements InventoryRedisPort {

    private final InventoryHoldRedisRepository
            inventoryHoldRedisRepository;

    private final RetryExecutor retryExecutor;

    private final TimeoutExecutor timeoutExecutor;

    private final ResilienceConfig resilienceConfig;

    private final InventoryMetrics inventoryMetrics;

    private final StructuredLogger structuredLogger;

    public InventoryRedisAdapter(
            InventoryHoldRedisRepository inventoryHoldRedisRepository,
            RetryExecutor retryExecutor,
            TimeoutExecutor timeoutExecutor,
            ResilienceConfig resilienceConfig,
            InventoryMetrics inventoryMetrics,
            StructuredLogger structuredLogger) {

        this.inventoryHoldRedisRepository =
                inventoryHoldRedisRepository;

        this.retryExecutor =
                retryExecutor;

        this.timeoutExecutor =
                timeoutExecutor;

        this.resilienceConfig =
                resilienceConfig;

        this.inventoryMetrics =
                inventoryMetrics;

        this.structuredLogger =
                structuredLogger;
    }

    @Override
    public void saveHold(
            ExecutionContext context,
            InventoryHold inventoryHold) {

        ExecutionIdentifier identifier =
                createRedisIdentifier(
                        context,
                        OperationType.WRITE,
                        inventoryHold
                );

        try {

            timeoutExecutor.execute(
                    identifier,
                    () -> retryExecutor.execute(
                            identifier,
                            () -> {

                                inventoryHoldRedisRepository
                                        .save(inventoryHold);

                                return null;
                            }
                    ),
                    resilienceConfig.getRedisTimeoutMs()
            );

            identifier.setOutcome(
                    successOutcome()
            );

        } catch (Exception ex) {

            identifier.setOutcome(
                    failureOutcome(ex)
            );

            throw ex;

        } finally {

            completeExecution(
                    context,
                    identifier
            );
        }
    }

    @Override
    public Optional<InventoryHold> findHoldByOrderId(
            ExecutionContext context,
            String orderId) {

        ExecutionIdentifier identifier =
                createRedisIdentifier(
                        context,
                        OperationType.READ,
                        orderId
                );

        try {

            Optional<InventoryHold> result =
                    timeoutExecutor.execute(
                            identifier,
                            () -> retryExecutor.execute(
                                    identifier,
                                    () -> inventoryHoldRedisRepository
                                            .findByOrderId(orderId)
                            ),
                            resilienceConfig.getRedisTimeoutMs()
                    );

            ExecutionIdentifier hitMissIdentifier =
                    createRedisIdentifier(
                            context,
                            result.isPresent()
                                    ? OperationType.HIT
                                    : OperationType.MISS,
                            orderId
                    );

            hitMissIdentifier.setOutcome(
                    successOutcome()
            );

            completeExecution(
                    context,
                    hitMissIdentifier
            );

            identifier.setOutcome(
                    successOutcome()
            );

            return result;

        } catch (Exception ex) {

            identifier.setOutcome(
                    failureOutcome(ex)
            );

            throw ex;

        } finally {

            completeExecution(
                    context,
                    identifier
            );
        }
    }

    @Override
    public List<InventoryHold> findExpiredHolds(
            ExecutionContext context) {

        ExecutionIdentifier identifier =
                createRedisIdentifier(
                        context,
                        OperationType.EXPIRE,
                        null
                );

        try {

            List<InventoryHold> result =
                    timeoutExecutor.execute(
                            identifier,
                            () -> retryExecutor.execute(
                                    identifier,
                                    inventoryHoldRedisRepository::findExpiredHolds
                            ),
                            resilienceConfig.getRedisTimeoutMs()
                    );

            identifier.setOutcome(
                    successOutcome()
            );

            return result;

        } catch (Exception ex) {

            identifier.setOutcome(
                    failureOutcome(ex)
            );

            throw ex;

        } finally {

            completeExecution(
                    context,
                    identifier
            );
        }
    }

    @Override
    public void deleteHold(
            ExecutionContext context,
            String orderId) {

        ExecutionIdentifier identifier =
                createRedisIdentifier(
                        context,
                        OperationType.DELETE,
                        orderId
                );

        try {

            timeoutExecutor.execute(
                    identifier,
                    () -> retryExecutor.execute(
                            identifier,
                            () -> {

                                inventoryHoldRedisRepository
                                        .delete(orderId);

                                return null;
                            }
                    ),
                    resilienceConfig.getRedisTimeoutMs()
            );

            identifier.setOutcome(
                    successOutcome()
            );

        } catch (Exception ex) {

            identifier.setOutcome(
                    failureOutcome(ex)
            );

            throw ex;

        } finally {

            completeExecution(
                    context,
                    identifier
            );
        }
    }

    private ExecutionIdentifier createRedisIdentifier(
            ExecutionContext context,
            OperationType operation,
            Object payload) {

        ExecutionIdentifier traceIdentifier =
                context.getIdentifier(
                        IdentifierType.TRACE
                );

        String parentIdentifier =
                traceIdentifier == null
                        ? null
                        : traceIdentifier.getIdentifier();

        return new ExecutionIdentifier(
                IdentifierType.REDIS,
                IdentifierBuilder.build(
                        IdentifierType.REDIS,
                        operation
                ),
                parentIdentifier,
                payload
        );
    }

    private OperationOutcome successOutcome() {

        return new OperationOutcome(
                OutcomeStatus.SUCCESS,
                FailureType.NONE,
                null,
                false
        );
    }

    private OperationOutcome failureOutcome(
            Exception ex) {

        return new OperationOutcome(
                OutcomeStatus.FAILURE,
                FailureTypeResolver.resolve(ex),
                ex.getMessage(),
                true
        );
    }

    private void completeExecution(
            ExecutionContext context,
            ExecutionIdentifier identifier) {

        identifier.markCompleted();

        context.addIdentifier(
                identifier
        );

        inventoryMetrics.record(
                identifier
        );

        structuredLogger.log(
                identifier
        );
    }
}