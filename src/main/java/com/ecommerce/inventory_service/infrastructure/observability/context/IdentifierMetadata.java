package com.ecommerce.inventory_service.infrastructure.observability.context;

public class IdentifierMetadata {

    private final String category;

    private final String operation;

    public IdentifierMetadata(
            String category,
            String operation) {

        this.category = category;
        this.operation = operation;
    }

    public String getCategory() {
        return category;
    }

    public String getOperation() {
        return operation;
    }
}