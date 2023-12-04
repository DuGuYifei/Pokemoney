package com.pokemoney.redis.service.provider.service;

import com.pokemoney.redis.service.api.exceptions.RedisRpcException;
import com.pokemoney.redis.service.api.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.StatusRpcException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is the service of operating on Redis.
 */
@Getter
@Service
@Slf4j
public class RedisService {
    /**
     * Object redis template.
     */
    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * Constructor of RedisService.
     *
     * @param redisTemplate Hash redis template.
     */
    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Delete the value from Redis.
     *
     * @param redisDelRequestDto The {@link RedisDelRequestDto} to be deleted.
     */
    public void del(RedisDelRequestDto redisDelRequestDto) {
        String key = redisDelRequestDto.getPrefix() + redisDelRequestDto.getKey();
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            log.error("Redis delete error.", e);
            throw new RpcException(RpcException.BIZ_EXCEPTION, "Redis delete error.");
        }
    }

    /**
     * Set the value into Redis.
     *
     * @param redisKeyValueDto The {@link RedisKeyValueDto} to be set.
     */
    public void setByDto(RedisKeyValueDto redisKeyValueDto) {
        String key = redisKeyValueDto.getPrefix() + redisKeyValueDto.getKey();
        if (!redisKeyValueDto.hasTimeout()) {
            try {
                redisTemplate.opsForValue().set(key, redisKeyValueDto.getValue());
            } catch (Exception e) {
                log.error("Redis set error.", e);
                throw new RpcException(RpcException.BIZ_EXCEPTION, "Redis set error.");
            }
            return;
        }
        redisTemplate.opsForValue().set(key, redisKeyValueDto.getValue(), redisKeyValueDto.getTimeout(), java.util.concurrent.TimeUnit.SECONDS);
    }

    /**
     * Get the value from Redis.
     *
     * @param redisKeyValueGetRequestDto The {@link RedisKeyValueDto} to get.
     * @return The value of the {@link RedisKeyValueDto} to get.
     */
    public RedisKeyValueDto getByDto(RedisKeyValueGetRequestDto redisKeyValueGetRequestDto){
        String key = redisKeyValueGetRequestDto.getPrefix() + redisKeyValueGetRequestDto.getKey();
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            throw new StatusRpcException(RedisRpcException.KEY_NOT_FOUND);
        }
        Long returnTimeout = redisTemplate.getExpire(key);
        if (returnTimeout == null) {
            returnTimeout = -2L;
        }
        return RedisKeyValueDto.newBuilder()
                .setKey(redisKeyValueGetRequestDto.getKey())
                .setValue((String) value)
                .setTimeout(returnTimeout)
                .setPrefix(redisKeyValueGetRequestDto.getPrefix())
                .build();
    }

    /**
     * Hash set the value into Redis.
     *
     * @param redisHashKeyValueDto The {@link RedisHashKeyValueDto} to be set.
     */
    public void hSetByDto(RedisHashKeyValueDto redisHashKeyValueDto) {
        String key = redisHashKeyValueDto.getPrefix() + redisHashKeyValueDto.getKey();
        if (! redisHashKeyValueDto.hasTimeout()) {
            try {
                redisTemplate.opsForHash().putAll(key, redisHashKeyValueDto.getValueMap());
            } catch (Exception e) {
                log.error("Redis set hash map error.", e);
                throw new RpcException(RpcException.BIZ_EXCEPTION, "Redis set hash map error.");
            }
            return;
        }
        redisTemplate.opsForHash().putAll(key, redisHashKeyValueDto.getValueMap());
    }

    /**
     * Hash get the value from Redis.
     *
     * @param redisHashKeyValueGetRequestDto The {@link RedisHashKeyValueGetRequestDto} to get.
     * @return The value of the {@link RedisHashKeyValueDto} to get.
     */
    public RedisHashKeyValueDto hGetByDto(RedisHashKeyValueGetRequestDto redisHashKeyValueGetRequestDto) {
        String key = redisHashKeyValueGetRequestDto.getPrefix() + redisHashKeyValueGetRequestDto.getKey();

        Map<Object, Object> value = redisTemplate.opsForHash().entries(key);
        Map<String, String> valueMap = new HashMap<>();
        for (Map.Entry<Object, Object> entry : value.entrySet()) {
            valueMap.put((String) entry.getKey(), (String) entry.getValue());
        }
        Long remainTime = redisTemplate.getExpire(key);
        if (remainTime == null) {
            remainTime = -2L;
        }
        return RedisHashKeyValueDto.newBuilder()
                .setKey(redisHashKeyValueGetRequestDto.getKey())
                .putAllValue(valueMap)
                .setTimeout(remainTime)
                .setPrefix(redisHashKeyValueGetRequestDto.getPrefix())
                .build();
    }
}
