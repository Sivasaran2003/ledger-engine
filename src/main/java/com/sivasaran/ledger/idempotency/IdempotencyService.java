package com.sivasaran.ledger.idempotency;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class IdempotencyService {

    private static final String PREFIX = "idempotency:";
    private static final long TTL_HOURS = 24;

    private final StringRedisTemplate redisTemplate;

    public IdempotencyService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String get(String reference) {
        return redisTemplate.opsForValue().get(PREFIX + reference);
    }

    public void store(
            String reference,
            UUID fromAccountId,
            UUID toAccountId,
            long amountPaise,
            UUID transactionId) {

        String value = fromAccountId
                + "|" + toAccountId
                + "|" + amountPaise
                + "|" + transactionId;

        redisTemplate.opsForValue().set(
                PREFIX + reference,
                value,
                TTL_HOURS,
                TimeUnit.HOURS
        );
    }
}