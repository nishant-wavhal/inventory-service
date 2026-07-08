package com.ecommerce.inventory_service.infrastructure.observability.context;

import java.util.UUID;

public final class IdentifierBuilder {

    private IdentifierBuilder() {
    }

    public static String build(
            IdentifierType type,
            OperationType operation) {

        return type.name()
                + "-"
                + operation.name()
                + "-"
                + UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase();
    }

  

    public static IdentifierMetadata resolve(
            String identifier) {

        String[] parts =
                identifier.split("-");

        if (parts.length < 3) {

            throw new IllegalArgumentException(
                    "Invalid identifier format: "
                            + identifier
            );
        }

        return new IdentifierMetadata(
                parts[0],
                parts[1]
        );
    }
}