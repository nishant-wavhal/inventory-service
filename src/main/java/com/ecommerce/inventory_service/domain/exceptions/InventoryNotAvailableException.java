package com.ecommerce.inventory_service.domain.exceptions;

public class InventoryNotAvailableException
        extends RuntimeException {

    public InventoryNotAvailableException(String message) {
        super(message);
    }
}