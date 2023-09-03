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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This class is the controller for register user.
 */
@RestController
@RequestMapping("/api/v1/")
public class RegisterUserController {
    /**
     * Redis service.
     */
    private final RedisService redisService;

    /**
     * Constructor.
     *
     * @param redisService Redis service.
     */
    public RegisterUserController(RedisService redisService) {
        this.redisService = redisService;
    }

    /**
     * Store email and verification code in redis.
     *
     * @param redisKeyValueDto The {@link RedisKeyValueDto} to be stored.
     * @return The {@link ResponseSuccessDto} of the result.
     */
    @PostMapping("/set")
    public <T> ResponseEntity<ResponseSuccessDto> setKeyValue(@Validated(RedisSetValueGroup.class) RedisKeyValueDto<T> redisKeyValueDto) {
        redisService.setByDto(redisKeyValueDto);
        ResponseSuccessDto responseSuccessDto = ResponseSuccessDto.builder()
                .message("Store successfully.")
                .status(200)
                .build();
        return ResponseEntity.ok(ResponseSuccessDto.builder().status(200).build());
    }

    /**
     * Get value from redis.
     *
     * @param redisKeyValueDto The {@link RedisKeyValueDto} to get.
     * @return The {@link ResponseSuccessDto} with data of {@link RedisKeyValueDto<T>}.
     */
    @PostMapping("/get")
    public <T> ResponseEntity<ResponseSuccessDto<RedisKeyValueDto<T>>> getKeyValue(@Validated(RedisGetValueGroup.class) RedisKeyValueDto<T> redisKeyValueDto) throws GenericNotFoundError {
        ResponseSuccessDto<RedisKeyValueDto<T>> responseSuccessDto = ResponseSuccessDto.<RedisKeyValueDto<T>>builder()
                .message("Get successfully.")
                .data(redisService.getByDto(redisKeyValueDto))
                .status(200)
                .build();
        return ResponseEntity.ok(responseSuccessDto);
    }
}
