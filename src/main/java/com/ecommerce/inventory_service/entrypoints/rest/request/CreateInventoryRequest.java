package com.ecommerce.inventory_service.entrypoints.rest.request;

public class CreateInventoryRequest {

    private String inventoryId;

    private String productId;

    private Integer availableQuantity;

    public CreateInventoryRequest() {
    }

    public CreateInventoryRequest(
            String inventoryId,
            String productId,
            Integer availableQuantity
    ) {
        this.inventoryId = inventoryId;
        this.productId = productId;
        this.availableQuantity = availableQuantity;
    }

    public String getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(String inventoryId) {
        this.inventoryId = inventoryId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }
}