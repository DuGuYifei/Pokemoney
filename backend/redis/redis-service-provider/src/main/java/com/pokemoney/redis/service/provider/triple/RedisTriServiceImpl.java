package com.pokemoney.redis.service.provider.triple;

import com.pokemoney.commons.proto.ResponseCommonPart;
import com.pokemoney.redis.service.api.*;
import com.pokemoney.redis.service.provider.service.RedisService;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * Redis service triple protocol endpoint.
 */
@DubboService(version = "1.0.0", protocol = "tri", group = "redis", timeout = 10000)
public class RedisTriServiceImpl implements RedisTriService {
    /**
     * Redis service.
     */
    private final RedisService redisService;

    /**
     * Constructor.
     *
     * @param redisService Redis service.
     */
    public RedisTriServiceImpl(RedisService redisService) {
        this.redisService = redisService;
    }
    /**
     * delete value.
     *
     * @param request {@link RedisDelRequestDto}
     * @return {@link RedisResponseDto}
     */
    @Override
    public RedisResponseDto del(RedisDelRequestDto request) {
        redisService.del(request);
        return RedisResponseDto.newBuilder()
                .setResponseCommonPart(
                        ResponseCommonPart.newBuilder()
                                .setMessage("Delete successfully.")
                                .setStatus(1)
                                .build()
                )
                .build();
    }

    /**
     * set string value.
     *
     * @param request {@link RedisKeyValueDto}
     * @return {@link RedisResponseDto}, data should be {@link RedisKeyValueDto}
     */
    @Override
    public RedisResponseDto set(RedisKeyValueDto request) {
        redisService.setByDto(request);
        return RedisResponseDto.newBuilder()
                .setResponseCommonPart(
                        ResponseCommonPart.newBuilder()
                                .setMessage("Set successfully.")
                                .setStatus(1)
                                .build()
                )
                .build();
    }

    /**
     * get string value.
     *
     * @param request {@link RedisKeyValueGetRequestDto}
     * @return {@link RedisResponseDto}, data should be {@link RedisKeyValueDto}
     */
    @Override
    public RedisResponseDto get(RedisKeyValueGetRequestDto request) {
        RedisKeyValueDto redisKeyValueDto = redisService.getByDto(request);
        return RedisResponseDto.newBuilder()
                .setResponseCommonPart(
                        ResponseCommonPart.newBuilder()
                                .setMessage("Get successfully.")
                                .setStatus(1)
                                .build()
                )
                .setData(redisKeyValueDto)
                .build();
    }

    /**
     * set hash value.
     *
     * @param request {@link RedisHashKeyValueDto}
     * @return {@link RedisResponseDto}
     */
    @Override
    public RedisResponseDto hSet(RedisHashKeyValueDto request) {
        redisService.hSetByDto(request);
        return RedisResponseDto.newBuilder()
                .setResponseCommonPart(
                        ResponseCommonPart.newBuilder()
                                .setMessage("HSet successfully.")
                                .setStatus(1)
                                .build()
                )
                .build();
    }

    /**
     * get hash value.
     *
     * @param request {@link RedisHashKeyValueGetRequestDto}
     * @return {@link RedisResponseDto}, data should be {@link RedisHashKeyValueDto}
     */
    @Override
    public RedisResponseDto hGet(RedisHashKeyValueGetRequestDto request) {
        RedisHashKeyValueDto redisHashKeyValueDto = redisService.hGetByDto(request);
        return RedisResponseDto.newBuilder()
                .setResponseCommonPart(
                        ResponseCommonPart.newBuilder()
                                .setMessage("HGet successfully.")
                                .setStatus(1)
                                .build()
                )
                .setHashData(redisHashKeyValueDto)
                .build();
    }
}
