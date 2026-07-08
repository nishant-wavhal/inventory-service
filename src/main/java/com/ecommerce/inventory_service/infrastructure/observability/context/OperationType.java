package com.ecommerce.inventory_service.infrastructure.observability.context;

public enum OperationType {

    READ,

    WRITE,

    UPDATE,

    DELETE,

    PUBLISH,

    CONSUME,

    HIT,

    MISS,

    EXPIRE
}