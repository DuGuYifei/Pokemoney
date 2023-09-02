package com.pokemoney.redisservice.service;

import com.pokemoney.redisservice.dto.RegisterVerificationDto;
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
     * Set the {@link RegisterVerificationDto} into Redis.
     *
     * @param registerVerificationDto The {@link RegisterVerificationDto} to be set.
     */
    public Boolean setRegisterVerificationDto(RegisterVerificationDto registerVerificationDto) {
        if (registerVerificationDto.getTimeout() == null) {
            return redisTemplate.opsForValue().setIfAbsent(registerVerificationDto.getEmail(), registerVerificationDto.getVerificationCode());
        }
        return redisTemplate.opsForValue().setIfAbsent(registerVerificationDto.getEmail(), registerVerificationDto.getVerificationCode(), registerVerificationDto.getTimeout(), java.util.concurrent.TimeUnit.SECONDS);
    }

    /**
     * Get the {@link RegisterVerificationDto} from Redis.
     *
     * @param registerVerificationDto The {@link RegisterVerificationDto} to get.
     * @return The verification code of the {@link RegisterVerificationDto} to get.
     */
    public Boolean verifyRegisterVerificationDto(RegisterVerificationDto registerVerificationDto) {
        Object value = redisTemplate.opsForValue().get(registerVerificationDto.getEmail());
        if (value == null) {
            return false;
        }
        return registerVerificationDto.getVerificationCode().equals(value);
    }
}
