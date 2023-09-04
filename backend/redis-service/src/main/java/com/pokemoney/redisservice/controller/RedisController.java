package com.pokemoney.redisservice.controller;

import com.pokemoney.commons.dto.RedisKeyValueDto;
import com.pokemoney.commons.dto.ResponseSuccessDto;
import com.pokemoney.commons.dto.validation.RedisGetValueGroup;
import com.pokemoney.commons.dto.validation.RedisSetValueGroup;
import com.pokemoney.commons.errors.GenericNotFoundError;
import com.pokemoney.redisservice.service.RedisService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Redis controller
 */
@RestController
@RequestMapping("/api/v1")
public class RedisController {
    /**
     * Redis service.
     */
    private final RedisService redisService;

    /**
     * Constructor.
     *
     * @param redisService Redis service.
     */
    public RedisController(RedisService redisService) {
        this.redisService = redisService;
    }

    /**
     * Store key and value code in redis.
     *
     * @param redisKeyValueDto The {@link RedisKeyValueDto} to be stored.
     * @return The {@link ResponseSuccessDto} of the result.
     */
    @PostMapping("/set")
    public ResponseEntity<ResponseSuccessDto> setKeyValue(@RequestBody @Validated(RedisSetValueGroup.class) RedisKeyValueDto redisKeyValueDto) {
        redisService.setByDto(redisKeyValueDto);
        ResponseSuccessDto responseSuccessDto = ResponseSuccessDto.builder()
                .message("Store successfully.")
                .status(200)
                .data(redisKeyValueDto)
                .build();
        return ResponseEntity.ok(responseSuccessDto);
    }

    /**
     * Get value from redis.
     *
     * @param redisKeyValueDto The {@link RedisKeyValueDto} to get.
     * @return The {@link ResponseSuccessDto} with data of {@link RedisKeyValueDto}.
     * @throws GenericNotFoundError If key or value not found or value is not expected type.
     */
    @PostMapping("/get")
    public ResponseEntity<ResponseSuccessDto> getKeyValue(@RequestBody @Validated(RedisGetValueGroup.class) RedisKeyValueDto redisKeyValueDto) throws GenericNotFoundError {
        redisKeyValueDto = redisService.getByDto(redisKeyValueDto);
        ResponseSuccessDto responseSuccessDto = ResponseSuccessDto.builder()
                .message("Get successfully.")
                .data(redisKeyValueDto)
                .status(200)
                .build();
        return ResponseEntity.ok(responseSuccessDto);
    }
}
