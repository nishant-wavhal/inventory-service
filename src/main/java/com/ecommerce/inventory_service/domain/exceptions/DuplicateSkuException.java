package com.ecommerce.inventory_service.domain.exceptions;

public class DuplicateSkuException extends RuntimeException {

    public DuplicateSkuException(String sku) {
        super("Inventory already exists for SKU: " + sku);
    }
}   