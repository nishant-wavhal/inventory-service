package com.ecommerce.inventory_service.infrastructure.database.adapter;

import com.ecommerce.inventory_service.application.port.ProcessedEventRepositoryPort;
import com.ecommerce.inventory_service.domain.model.ProcessedEvent;
import com.ecommerce.inventory_service.infrastructure.database.entity.ProcessedEventEntity;
import com.ecommerce.inventory_service.infrastructure.database.repository.ProcessedEventJpaRepository;
import com.ecommerce.inventory_service.infrastructure.observability.context.*;
import com.ecommerce.inventory_service.infrastructure.observability.logging.StructuredLogger;
import com.ecommerce.inventory_service.infrastructure.observability.metrics.InventoryMetrics;
import com.ecommerce.inventory_service.infrastructure.observability.outcome.*;
import com.ecommerce.inventory_service.infrastructure.resilience.ResilienceConfig;
import com.ecommerce.inventory_service.infrastructure.resilience.retry.RetryExecutor;
import com.ecommerce.inventory_service.infrastructure.resilience.timeout.TimeoutExecutor;
import com.ecommerce.inventory_service.shared.mapper.ProcessedEventMapper;

import java.util.Objects;

import org.springframework.stereotype.Component;

@Component
public class ProcessedEventRepositoryAdapter
        implements ProcessedEventRepositoryPort {

    private final ProcessedEventJpaRepository processedEventJpaRepository;

    private final RetryExecutor retryExecutor;

    private final TimeoutExecutor timeoutExecutor;

    private final ResilienceConfig resilienceConfig;

    private final InventoryMetrics inventoryMetrics;

    private final StructuredLogger structuredLogger;

    public ProcessedEventRepositoryAdapter(
            ProcessedEventJpaRepository processedEventJpaRepository,
            RetryExecutor retryExecutor,
            TimeoutExecutor timeoutExecutor,
            ResilienceConfig resilienceConfig,
            InventoryMetrics inventoryMetrics,
            StructuredLogger structuredLogger) {

        this.processedEventJpaRepository =
                processedEventJpaRepository;

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
    public ProcessedEvent save(
            ExecutionContext context,
            ProcessedEvent processedEvent) {

        ExecutionIdentifier identifier =
                createDatabaseIdentifier(
                        context,
                        OperationType.WRITE,
                        processedEvent
                );

        try {

            ProcessedEvent result =
                    timeoutExecutor.execute(
                            identifier,
                            () -> retryExecutor.execute(
                                    identifier,
                                    () -> {

                                        ProcessedEventEntity entity =
                                                ProcessedEventMapper.toEntity(
                                                        processedEvent
                                                );

                                        ProcessedEventEntity savedEntity =
                                                processedEventJpaRepository.save(
                                                        Objects.requireNonNull(
                                                                entity
                                                        )
                                                );

                                        return ProcessedEventMapper.toDomain(
                                                savedEntity
                                        );
                                    }
                            ),
                            resilienceConfig.getDatabaseTimeoutMs()
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
    public boolean existsByEventId(
            ExecutionContext context,
            String eventId) {

        ExecutionIdentifier identifier =
                createDatabaseIdentifier(
                        context,
                        OperationType.READ,
                        eventId
                );

        try {

            boolean result =
                    timeoutExecutor.execute(
                            identifier,
                            () -> retryExecutor.execute(
                                    identifier,
                                    () -> processedEventJpaRepository
                                            .existsByEventId(
                                                    eventId
                                            )
                            ),
                            resilienceConfig.getDatabaseTimeoutMs()
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