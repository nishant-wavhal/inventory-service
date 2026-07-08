package com.ecommerce.inventory_service.shared.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public final class JsonUtil {

    private static final ObjectMapper OBJECT_MAPPER =
            new ObjectMapper();

    static {
        OBJECT_MAPPER.registerModule(
                new JavaTimeModule());
    }

    private JsonUtil() {
    }

    public static String toJson(
            Object object) {

        try {

            return OBJECT_MAPPER
                    .writeValueAsString(object);

        } catch (JsonProcessingException ex) {

            throw new IllegalStateException(
                    "Failed to serialize object",
                    ex);
        }
    }

    public static <T> T fromJson(
            String json,
            Class<T> clazz) {

        try {

            return OBJECT_MAPPER
                    .readValue(json, clazz);

        } catch (Exception ex) {

            throw new IllegalStateException(
                    "Failed to deserialize json",
                    ex);
        }
    }
}