package com.ecommerce.inventory_service.infrastructure.observability.outcome;

import java.sql.SQLException;
import java.util.concurrent.TimeoutException;

public final class FailureTypeResolver {

    private FailureTypeResolver() {
    }

    public static FailureType resolve(
            Throwable throwable) {

        if (throwable == null) {

            return FailureType.UNKNOWN;
        }

        if (throwable instanceof TimeoutException) {

            return FailureType.TIMEOUT;
        }

        if (throwable instanceof SQLException) {

            return FailureType.DATABASE_FAILURE;
        }

        String exceptionName =
                throwable.getClass()
                        .getSimpleName()
                        .toUpperCase();

        if (exceptionName.contains("REDIS")) {

            return FailureType.REDIS_FAILURE;
        }

        if (exceptionName.contains("KAFKA")) {

            return FailureType.KAFKA_FAILURE;
        }

        if (exceptionName.contains("SERIAL")) {

            return FailureType.SERIALIZATION_FAILURE;
        }

        if (exceptionName.contains("CONFIG")) {

            return FailureType.CONFIGURATION_FAILURE;
        }

        if (exceptionName.contains("AUTH")) {

            return FailureType.AUTHORIZATION_FAILURE;
        }

        return FailureType.UNKNOWN;
    }
}