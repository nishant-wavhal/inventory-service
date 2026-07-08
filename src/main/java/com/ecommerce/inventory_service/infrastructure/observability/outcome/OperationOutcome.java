package com.ecommerce.inventory_service.infrastructure.observability.outcome;

public class OperationOutcome {

    private final OutcomeStatus status;


    private final FailureType failureType;

    private final String failureReason;

    private final boolean compensatable;

    public OperationOutcome(
            OutcomeStatus status,
            FailureType failureType,
            String failureReason,
            boolean compensatable) {

        this.status = status;
        this.failureType = failureType;
        this.failureReason = failureReason;
        this.compensatable = compensatable;
    }

    public OutcomeStatus getStatus() {
        return status;
    }

    public FailureType getFailureType() {
        return failureType;
    }

    public String getFailureReason() {
        return failureReason;
    }

    public boolean isCompensatable() {
        return compensatable;
    }
    
}