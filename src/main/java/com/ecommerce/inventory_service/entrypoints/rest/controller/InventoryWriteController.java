package com.ecommerce.inventory_service.entrypoints.rest.controller;

//import com.ecommerce.inventory_service.application.port.CreateInventoryUseCase;
//import com.ecommerce.inventory_service.domain.model.Inventory;
//import com.ecommerce.inventory_service.entrypoints.request.CreateInventoryRequest;
//import com.ecommerce.inventory_service.entrypoints.response.InventoryResponse;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryWriteController {

   /*  private final CreateInventoryUseCase createInventoryUseCase;

    public InventoryWriteController(
            CreateInventoryUseCase createInventoryUseCase
    ) {
        this.createInventoryUseCase = createInventoryUseCase;
    }

    @PostMapping
    public InventoryResponse createInventory(
            @RequestBody CreateInventoryRequest request
    ) {

        Inventory inventory = new Inventory(
                request.getInventoryId(),
                request.getProductId(),
                request.getAvailableQuantity(),
                0,
                0L
        );

        Inventory savedInventory =
                createInventoryUseCase.createInventory(inventory);

        return new InventoryResponse(
                savedInventory.getInventoryId(),
                savedInventory.getProductId(),
                savedInventory.getAvailableQuantity(),
                savedInventory.getReservedQuantity()
        );
    }*/
}