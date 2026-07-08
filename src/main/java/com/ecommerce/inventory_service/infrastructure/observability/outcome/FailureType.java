package com.ecommerce.inventory_service.infrastructure.observability.outcome;

public enum FailureType {

    NONE,

    VALIDATION_FAILURE,

    BUSINESS_FAILURE,

    DATABASE_FAILURE,

    REDIS_FAILURE,

    KAFKA_FAILURE,

    TIMEOUT,

    SERIALIZATION_FAILURE,

    CONFIGURATION_FAILURE,

    AUTHORIZATION_FAILURE,

    UNKNOWN
}