package com.pokemoney.redis.service.api.exceptions;

import org.apache.dubbo.rpc.TriRpcStatus;

/**
 * Redis dubbo protocol exceptions throw by the API.
 */
public class RedisRpcException extends TriRpcStatus {
    /**
     * Key not found exception.
     */
    public static final RedisRpcException KEY_NOT_FOUND = new RedisRpcException(Code.NOT_FOUND, null, "Key not found.");

    /**
     * Constructor.
     */
    public RedisRpcException(Code code, Throwable cause, String description) {
        super(code, cause, description);
    }

    /**
     * equals.
     *
     * @param o object
     * @return true if equals
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof TriRpcStatus that) {
            if (this.code.code != that.code.code) {
                return false;
            }
            return this.description.equals(that.description);
        }else {
            return false;
        }
    }
}
