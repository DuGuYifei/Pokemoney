package com.pokemoney.redis.service.triple;

import com.google.protobuf.Any;
import com.pokemoney.commons.proto.Response;
import com.pokemoney.redisservice.api.*;
import com.pokemoney.redis.service.service.RedisService;
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
     * @return {@link Response}
     */
    @Override
    public Response del(RedisDelRequestDto request) {
        redisService.del(request);
        return Response.newBuilder().setMessage("Delete successfully.").setStatus(1).build();
    }

    /**
     * set string value.
     *
     * @param request {@link RedisKeyValueDto}
     * @return {@link Response}, data should be {@link RedisKeyValueDto}
     */
    @Override
    public Response set(RedisKeyValueDto request) {
        redisService.setByDto(request);
        return Response.newBuilder().setMessage("Set successfully.").setStatus(1).build();
    }

    /**
     * get string value.
     *
     * @param request {@link RedisKeyValueGetRequestDto}
     * @return {@link Response}, data should be {@link RedisKeyValueDto}
     */
    @Override
    public Response get(RedisKeyValueGetRequestDto request) {
        RedisKeyValueDto redisKeyValueDto = redisService.getByDto(request);
        Any anyData = Any.pack(redisKeyValueDto);
        return Response.newBuilder().setMessage("Get successfully.").setData(anyData).setStatus(1).build();
    }

    /**
     * set hash value.
     *
     * @param request {@link RedisHashKeyValueDto}
     * @return {@link Response}
     */
    @Override
    public Response hSet(RedisHashKeyValueDto request) {
        redisService.hSetByDto(request);
        return Response.newBuilder().setMessage("HSet successfully.").setStatus(1).build();
    }

    /**
     * get hash value.
     *
     * @param request {@link RedisHashKeyValueGetRequestDto}
     * @return {@link Response}, data should be {@link RedisHashKeyValueDto}
     */
    @Override
    public Response hGet(RedisHashKeyValueGetRequestDto request) {
        RedisHashKeyValueDto redisHashKeyValueDto = redisService.hGetByDto(request);
        Any anyData = Any.pack(redisHashKeyValueDto);
        return Response.newBuilder().setMessage("HGet successfully.").setData(anyData).setStatus(1).build();
    }
}
