package com.ecommerce.inventory_service.domain.decisions;

public class DecisionResult {

    private final DecisionStatus status;
    private final String reasonCode;
    private final String message;

    public DecisionResult(
            DecisionStatus status,
            String reasonCode,
            String message) {
        this.status = status;
        this.reasonCode = reasonCode;
        this.message = message;
    }

    public DecisionStatus getStatus() {
        return status;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public String getMessage() {
        return message;
    }

    public boolean isApproved() {
        return DecisionStatus.APPROVED.equals(status);
    }

    public boolean isRejected() {
        return DecisionStatus.REJECTED.equals(status);
    }
}