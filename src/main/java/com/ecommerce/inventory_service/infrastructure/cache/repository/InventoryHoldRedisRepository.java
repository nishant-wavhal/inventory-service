package com.ecommerce.inventory_service.infrastructure.cache.repository;

import com.ecommerce.inventory_service.domain.model.InventoryHold;
import com.ecommerce.inventory_service.shared.util.JsonUtil;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Repository
public class InventoryHoldRedisRepository {

    private static final String KEY_PREFIX =
            "inventory:hold:";

    private final RedisTemplate<String, String>
            redisTemplate;

    public InventoryHoldRedisRepository(
            RedisTemplate<String, String> redisTemplate) {

        this.redisTemplate = redisTemplate;
    }

    public void save(
            InventoryHold inventoryHold) {

        String key =
                buildKey(
                        inventoryHold.getOrderId());

        String value =
                JsonUtil.toJson(inventoryHold);

        redisTemplate.opsForValue()
                .set(Objects.requireNonNull(key), Objects.requireNonNull(value));
    }

    public Optional<InventoryHold> findByOrderId(
            String orderId) {

        String key = buildKey(orderId);

        String value =
                redisTemplate.opsForValue()
                        .get(Objects.requireNonNull(key));

        if (value == null) {
            return Optional.empty();
        }

        InventoryHold inventoryHold =
                JsonUtil.fromJson(
                        value,
                        InventoryHold.class);

        return Optional.of(inventoryHold);
    }

    public List<InventoryHold> findExpiredHolds() {

        Set<String> keys =
                redisTemplate.keys(
                        KEY_PREFIX + "*");

        List<InventoryHold> expiredHolds =
                new ArrayList<>();

        if (keys == null || keys.isEmpty()) {
            return expiredHolds;
        }

        for (String key : keys) {

            String value =
                    redisTemplate.opsForValue()
                            .get(Objects.requireNonNull(key));

            if (value == null) {
                continue;
            }

            InventoryHold inventoryHold =
                    JsonUtil.fromJson(
                            value,
                            InventoryHold.class);

            if (inventoryHold.isExpired()) {

                expiredHolds.add(
                        inventoryHold);
            }
        }

        return expiredHolds;
    }

    public void delete(
            String orderId) {

        redisTemplate.delete(
                Objects.requireNonNull(buildKey(orderId)));
    }

    private String buildKey(
            String orderId) {

        return KEY_PREFIX + orderId;
    }
}