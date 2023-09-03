package com.pokemoney.redisservice.service;

import com.pokemoney.commons.dto.RedisKeyValueDto;
import com.pokemoney.commons.errors.GenericNotFoundError;
import lombok.Getter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * This class is the service of operating on Redis.
 */
@Service
public class RedisService {
    /**
     * Object redis template.
     */
    @Getter
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
     * Set the value into Redis.
     *
     * @param redisKeyValueDto<T> The {@link RedisKeyValueDto<T>} to be set.
     */
    public <T> void setByDto(RedisKeyValueDto<T> redisKeyValueDto) {
        String key = redisKeyValueDto.getPrefix() + redisKeyValueDto.getKey();
        if (redisKeyValueDto.getTimeout() == null) {
            redisTemplate.opsForValue().set(key, redisKeyValueDto.getValue());
        }
        redisTemplate.opsForValue().set(key, redisKeyValueDto.getValue(), redisKeyValueDto.getTimeout(), java.util.concurrent.TimeUnit.SECONDS);
    }

    /**
     * Get the value from Redis.
     *
     * @param redisKeyValueDto<T> The {@link RedisKeyValueDto<T>} to get.
     * @return The value of the {@link RedisKeyValueDto<T>} to get.
     */
    public <T> RedisKeyValueDto<T> getByDto(RedisKeyValueDto<T> redisKeyValueDto) throws GenericNotFoundError {
        String key = redisKeyValueDto.getPrefix() + redisKeyValueDto.getKey();
        Long remainTime = redisTemplate.getExpire(key);
        if (remainTime == -2) {
            throw new GenericNotFoundError("Key not found.");
        }
        redisKeyValueDto.setTimeout(remainTime);
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            throw new GenericNotFoundError("Value not found.");
        }
        try{
            T t = (T) value;
            redisKeyValueDto.setValue(t);
        } catch (ClassCastException e) {
            throw new GenericNotFoundError("Value of the key is not the type you wanted.");
        }
        return redisKeyValueDto;
    }
}
