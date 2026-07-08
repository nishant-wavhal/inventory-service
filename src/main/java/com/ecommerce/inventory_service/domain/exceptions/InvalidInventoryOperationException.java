package com.ecommerce.inventory_service.domain.exceptions;

public class InvalidInventoryOperationException
        extends RuntimeException {

    public InvalidInventoryOperationException(String message) {
        super(message);
    }
}