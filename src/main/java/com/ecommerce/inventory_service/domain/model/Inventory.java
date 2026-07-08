package com.ecommerce.inventory_service.domain.model;

import com.ecommerce.inventory_service.domain.enums.InventoryStatus;
import com.ecommerce.inventory_service.domain.exceptions.InventoryNotAvailableException;
import com.ecommerce.inventory_service.domain.exceptions.InvalidInventoryOperationException;

public class Inventory {

    private Long id;
    private String sku;
    private Integer availableQuantity;
    private Integer reservedQuantity;
    private InventoryStatus status;
    private Long version;

    public Inventory() {
    }

    public Inventory(
            Long id,
            String sku,
            Integer availableQuantity,
            Integer reservedQuantity,
            InventoryStatus status,
            Long version) {
        this.id = id;
        this.sku = sku;
        this.availableQuantity = availableQuantity;
        this.reservedQuantity = reservedQuantity;
        this.status = status;
        this.version = version;
    }

    public void reserve(Integer quantity) {

        if (availableQuantity < quantity) {
            throw new InventoryNotAvailableException(
                    "Insufficient inventory for sku: " + sku);
        }

        availableQuantity -= quantity;
        reservedQuantity += quantity;
    }

    public void release(Integer quantity) {

        if (reservedQuantity < quantity) {
            throw new InvalidInventoryOperationException(
                    "Reserved quantity insufficient for sku: " + sku);
        }

        reservedQuantity -= quantity;
        availableQuantity += quantity;
    }

    public void deduct(Integer quantity) {

        if (reservedQuantity < quantity) {
            throw new InvalidInventoryOperationException(
                    "Reserved quantity insufficient for sku: " + sku);
        }

        reservedQuantity -= quantity;
    }

    public Long getId() {
        return id;
    }

    public String getSku() {
        return sku;
    }

    public Integer getAvailableQuantity() {
        return availableQuantity;
    }

    public Integer getReservedQuantity() {
        return reservedQuantity;
    }

    public InventoryStatus getStatus() {
        return status;
    }

    public Long getVersion() {
        return version;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public void setAvailableQuantity(Integer availableQuantity) {
        this.availableQuantity = availableQuantity;
    }

    public void setReservedQuantity(Integer reservedQuantity) {
        this.reservedQuantity = reservedQuantity;
    }

    public void setStatus(InventoryStatus status) {
        this.status = status;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}