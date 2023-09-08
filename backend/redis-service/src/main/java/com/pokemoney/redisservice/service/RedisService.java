package com.pokemoney.redisservice.service;

import com.pokemoney.commons.dto.RedisKeyValueDto;
import com.pokemoney.commons.errors.GenericInternalServerError;
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
     * @param redisKeyValueDto The {@link RedisKeyValueDto} to be set.
     * @throws GenericInternalServerError If redis set error.
     */
    public void setByDto(RedisKeyValueDto redisKeyValueDto) throws GenericInternalServerError {
        String key = redisKeyValueDto.getPrefix() + redisKeyValueDto.getKey();
        if (redisKeyValueDto.getTimeout() == null) {
            try {
                redisTemplate.opsForValue().set(key, redisKeyValueDto.getValue());
            } catch (Exception e) {
                throw new GenericInternalServerError("Redis set error.");
            }
            return;
        }
        redisTemplate.opsForValue().set(key, redisKeyValueDto.getValue(), redisKeyValueDto.getTimeout(), java.util.concurrent.TimeUnit.SECONDS);
    }

    /**
     * Get the value from Redis.
     *
     * @param redisKeyValueDto The {@link RedisKeyValueDto} to get.
     * @return <RequestRegisterUserDto>The value of the {@link RedisKeyValueDto} to get.
     */
    public RedisKeyValueDto getByDto(RedisKeyValueDto redisKeyValueDto) throws GenericNotFoundError {
        String key = redisKeyValueDto.getPrefix() + redisKeyValueDto.getKey();
        Long remainTime = redisTemplate.getExpire(key);
        if (remainTime != null) {
            if (remainTime == -2L) {
                throw new GenericNotFoundError("Key not found.");
            }
            redisKeyValueDto.setTimeout(remainTime);
        }
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            throw new GenericNotFoundError("Key not found.");
        }
        redisKeyValueDto.setValue((String) value);
        return redisKeyValueDto;
    }
}
