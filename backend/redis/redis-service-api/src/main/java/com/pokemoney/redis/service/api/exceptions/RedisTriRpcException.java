package com.pokemoney.redis.service.api.exceptions;

import org.apache.dubbo.rpc.RpcException;

/**
 * Redis dubbo protocol exceptions throw by the API.
 */
public class RedisTriRpcException extends RpcException {
    public static int KEY_NOT_FOUND = 10001;

    /**
     * Constructor.
     *
     * @param code    Error code.
     * @param message Error message.
     */
    public RedisTriRpcException(int code, String message) {
        super(code, message);
    }
}
