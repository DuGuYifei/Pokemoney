package com.pokemoney.user.service.api.exceptions;

import org.apache.dubbo.rpc.TriRpcStatus;

/**
 * User triple protocol exceptions throw by the API.
 */
public class UserTriRpcException extends TriRpcStatus {
    /**
     * Invalid token.
     */
    public static final UserTriRpcException INVALID_TOKEN = new UserTriRpcException(Code.PERMISSION_DENIED, null, "For safety, session is expired or invalid. Please re-login.");
    /**
     * Invalid user.
     */
    public static final UserTriRpcException INVALID_USER = new UserTriRpcException(Code.PERMISSION_DENIED, null, "Invalid user for this token.");
    /**
     * Permission denied.
     */
    public static final UserTriRpcException LOW_PERMISSION = new UserTriRpcException(Code.PERMISSION_DENIED, null, "Permission denied.");
    /**
     * Role denied.
     */
    public static final UserTriRpcException WRONG_ROLE = new UserTriRpcException(Code.PERMISSION_DENIED, null, "Role denied.");
    /**
     * User not found.
     */
    public static final UserTriRpcException USER_NOT_FOUND = new UserTriRpcException(Code.NOT_FOUND, null, "User not found.");

    /**
     * Constructor.
     */
    public UserTriRpcException(Code code, Throwable cause, String description) {
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
        } else {
            return false;
        }
    }
}