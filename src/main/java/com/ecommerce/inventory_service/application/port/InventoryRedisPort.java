package com.ecommerce.inventory_service.application.port;

import com.ecommerce.inventory_service.domain.model.InventoryHold;
import com.ecommerce.inventory_service.infrastructure.observability.context.ExecutionContext;

import java.util.List;
import java.util.Optional;

public interface InventoryRedisPort {

    void saveHold(ExecutionContext context,InventoryHold inventoryHold);

    Optional<InventoryHold> findHoldByOrderId(ExecutionContext context,String orderId);

    List<InventoryHold> findExpiredHolds(ExecutionContext context);

    void deleteHold(ExecutionContext context,String orderId);
}