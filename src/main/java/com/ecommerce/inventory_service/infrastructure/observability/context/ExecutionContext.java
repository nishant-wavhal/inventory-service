package com.ecommerce.inventory_service.infrastructure.observability.context;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

public class ExecutionContext {

    /*
     * ---------------------------------------------------------
     * Active Execution Context
     * ---------------------------------------------------------
     *
     * Only one Saga is active inside a service boundary.
     *
     * A Saga may contain multiple Traces, but only one Trace
     * is active at a time within the current service.
     *
     * These references are automatically maintained by the
     * execution lifecycle.
     */
    private ExecutionIdentifier activeSaga;

    private ExecutionIdentifier activeTrace;

    /*
     * ---------------------------------------------------------
     * Runtime Execution Stack
     * ---------------------------------------------------------
     *
     * Maintains the current execution hierarchy.
     *
     * Example:
     *
     * TOP
     * DATABASE
     * TRACE
     * SAGA
     *
     * Used only for lifecycle ordering.
     */
    private final Deque<ExecutionIdentifier> executionStack;

    /*
     * ---------------------------------------------------------
     * Execution History
     * ---------------------------------------------------------
     *
     * Stores every completed execution.
     *
     * Used for:
     *
     * - Metrics
     * - Structured Logs
     * - Aggregate Outcome
     * - Future Compensation Analysis
     */
    private final List<ExecutionIdentifier> executionHistory;

    public ExecutionContext() {

        this.executionStack =
                new ArrayDeque<>();

        this.executionHistory =
                new ArrayList<>();
    }

    /*
     * ---------------------------------------------------------
     * Begin Execution
     * ---------------------------------------------------------
     *
     * Pushes execution onto the runtime stack.
     *
     * Updates the active hierarchy automatically.
     */
    public void push(
            ExecutionIdentifier identifier) {

        executionStack.push(identifier);

        switch (identifier.getType()) {

            case SAGA:
                activeSaga = identifier;
                break;

            case TRACE:
                activeTrace = identifier;
                break;

            default:
                break;
        }
    }

    /*
     * ---------------------------------------------------------
     * Complete Execution
     * ---------------------------------------------------------
     *
     * Removes execution from the runtime hierarchy.
     *
     * Stores completed execution in history.
     *
     * Clears active Saga / Trace automatically.
     */
    public ExecutionIdentifier pop() {

        ExecutionIdentifier identifier =
                executionStack.pop();

        identifier.markCompleted();

        executionHistory.add(identifier);

        switch (identifier.getType()) {

            case TRACE:
                activeTrace = null;
                break;

            case SAGA:
                activeSaga = null;
                break;

            default:
                break;
        }

        return identifier;
    }

    /*
     * ---------------------------------------------------------
     * Current Execution
     * ---------------------------------------------------------
     */
    public ExecutionIdentifier peek() {

        return executionStack.peek();
    }

    public boolean isEmpty() {

        return executionStack.isEmpty();
    }

    /*
     * ---------------------------------------------------------
     * Active Context
     * ---------------------------------------------------------
     */

    public ExecutionIdentifier getActiveSaga() {

        return activeSaga;
    }

    public ExecutionIdentifier getActiveTrace() {

        return activeTrace;
    }

    /*
     * ---------------------------------------------------------
     * Runtime Hierarchy
     * ---------------------------------------------------------
     */

    public List<ExecutionIdentifier>
    getExecutionStack() {

        return List.copyOf(executionStack);
    }

    /*
     * ---------------------------------------------------------
     * Execution History
     * ---------------------------------------------------------
     */

    public List<ExecutionIdentifier>
    getExecutionHistory() {

        return Collections.unmodifiableList(
                executionHistory
        );
    }
}