package com.pokemoney.userservice.feignclient;

import com.pokemoney.commons.dto.RedisKeyValueDto;
import com.pokemoney.commons.dto.ResponseSuccessDto;
import com.pokemoney.commons.dto.validation.RedisGetValueGroup;
import com.pokemoney.commons.dto.validation.RedisSetValueGroup;
import com.pokemoney.commons.errors.GenericNotFoundError;
import com.pokemoney.userservice.Constants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.Serializable;

/**
 * Leaf client for feign client of leaf-service.
 * Setup strategy for feign client in application properties.
 */
@FeignClient(name = Constants.REDIS_IN_EUREKA_KEY)
public interface RedisClient {
    /**
     * Store key, value and timeout in redis.
     *
     * @param redisKeyValueDto The {@link RedisKeyValueDto} to be stored.
     * @return The {@link ResponseSuccessDto} of the result.
     */
    @PostMapping("/api/v1/set")
    ResponseEntity<ResponseSuccessDto> setKeyValue(RedisKeyValueDto redisKeyValueDto);

    /**
     * Get value from redis.
     *
     * @param redisKeyValueDto The {@link RedisKeyValueDto} to get.
     * @return The {@link ResponseSuccessDto} with data of {@link RedisKeyValueDto}.
     * @throws GenericNotFoundError If key or value not found.
     */
    @PostMapping("/api/v1/get")
    ResponseEntity<ResponseSuccessDto> getKeyValue(RedisKeyValueDto redisKeyValueDto) throws GenericNotFoundError;
}
