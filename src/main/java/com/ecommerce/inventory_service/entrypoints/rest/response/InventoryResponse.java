package com.ecommerce.inventory_service.entrypoints.rest.response;

public class InventoryResponse {

    private String inventoryId;

    private String productId;

    private Integer availableQuantity;

    private Integer reservedQuantity;

    public InventoryResponse() {
    }

    public InventoryResponse(
            String inventoryId,
            String productId,
            Integer availableQuantity,
            Integer reservedQuantity
    ) {
        this.inventoryId = inventoryId;
        this.productId = productId;
        this.availableQuantity = availableQuantity;
        this.reservedQuantity = reservedQuantity;
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

    public Integer getReservedQuantity() {
        return reservedQuantity;
    }

    public void setReservedQuantity(Integer reservedQuantity) {
        this.reservedQuantity = reservedQuantity;
    }
}