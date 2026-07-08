package com.ecommerce.inventory_service.infrastructure.database.adapter;

import com.ecommerce.inventory_service.application.port.InventoryRepositoryPort;
import com.ecommerce.inventory_service.domain.model.Inventory;
import com.ecommerce.inventory_service.infrastructure.database.entity.InventoryEntity;
import com.ecommerce.inventory_service.infrastructure.database.repository.InventoryJpaRepository;
import com.ecommerce.inventory_service.infrastructure.observability.context.*;
import com.ecommerce.inventory_service.infrastructure.observability.logging.StructuredLogger;
import com.ecommerce.inventory_service.infrastructure.observability.metrics.InventoryMetrics;
import com.ecommerce.inventory_service.infrastructure.observability.outcome.*;
import com.ecommerce.inventory_service.infrastructure.resilience.ResilienceConfig;
import com.ecommerce.inventory_service.infrastructure.resilience.retry.RetryExecutor;
import com.ecommerce.inventory_service.infrastructure.resilience.timeout.TimeoutExecutor;
import com.ecommerce.inventory_service.shared.mapper.InventoryMapper;

import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Optional;

@Component
public class InventoryRepositoryAdapter
        implements InventoryRepositoryPort {

    private final InventoryJpaRepository inventoryJpaRepository;

    private final RetryExecutor retryExecutor;

    private final TimeoutExecutor timeoutExecutor;

    private final ResilienceConfig resilienceConfig;

    private final InventoryMetrics inventoryMetrics;

    private final StructuredLogger structuredLogger;

    public InventoryRepositoryAdapter(
            InventoryJpaRepository inventoryJpaRepository,
            RetryExecutor retryExecutor,
            TimeoutExecutor timeoutExecutor,
            ResilienceConfig resilienceConfig,
            InventoryMetrics inventoryMetrics,
            StructuredLogger structuredLogger) {

        this.inventoryJpaRepository =
                inventoryJpaRepository;

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
    public Inventory save(
            ExecutionContext context,
            Inventory inventory) {

        ExecutionIdentifier identifier =
                createDatabaseIdentifier(
                        context,
                        OperationType.WRITE,
                        inventory
                );

        try {

            Inventory result =
                    timeoutExecutor.execute(
                            identifier,
                            () -> retryExecutor.execute(
                                    identifier,
                                    () -> {

                                        InventoryEntity entity =
                                                InventoryMapper.toEntity(
                                                        inventory
                                                );

                                        InventoryEntity savedEntity =
                                                inventoryJpaRepository.save(
                                                        Objects.requireNonNull(
                                                                entity
                                                        )
                                                );

                                        return InventoryMapper.toDomain(
                                                savedEntity
                                        );
                                    }
                            ),
                            resilienceConfig
                                    .getDatabaseTimeoutMs()
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
    public Optional<Inventory> findById(
            ExecutionContext context,
            Long id) {

        ExecutionIdentifier identifier =
                createDatabaseIdentifier(
                        context,
                        OperationType.READ,
                        id
                );

        try {

            Optional<Inventory> result =
                    timeoutExecutor.execute(
                            identifier,
                            () -> retryExecutor.execute(
                                    identifier,
                                    () -> inventoryJpaRepository
                                            .findById(
                                                    Objects.requireNonNull(id)
                                            )
                                            .map(
                                                    InventoryMapper::toDomain
                                            )
                            ),
                            resilienceConfig
                                    .getDatabaseTimeoutMs()
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
    public Optional<Inventory> findBySku(
            ExecutionContext context,
            String sku) {

        ExecutionIdentifier identifier =
                createDatabaseIdentifier(
                        context,
                        OperationType.READ,
                        sku
                );

        try {

            Optional<Inventory> result =
                    timeoutExecutor.execute(
                            identifier,
                            () -> retryExecutor.execute(
                                    identifier,
                                    () -> inventoryJpaRepository
                                            .findBySku(sku)
                                            .map(
                                                    InventoryMapper::toDomain
                                            )
                            ),
                            resilienceConfig
                                    .getDatabaseTimeoutMs()
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

    private ExecutionIdentifier createDatabaseIdentifier(
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
                IdentifierType.DATABASE,
                IdentifierBuilder.build(
                        IdentifierType.DATABASE,
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