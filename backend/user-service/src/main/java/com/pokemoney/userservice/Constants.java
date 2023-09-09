package com.pokemoney.userservice;

import lombok.Getter;


/**
 * This class is the constants of user-service.
 */
@Getter
public class Constants {
    /**
     * The name of current service.
     */
    public static final String SERVICE_NAME = "user-service";

    /**
     * The key of leaf-service in eureka-service.
     */
    public static final String LEAF_IN_EUREKA_KEY = "leaf-service";

    /**
     * The key of redis-service in eureka-service.
     */
    public static final String REDIS_IN_EUREKA_KEY = "redis-service";

    /**
     * The key of user-service in leaf-service.
     */
    public static final String USER_IN_LEAF_KEY = SERVICE_NAME;

    /**
     * The length of verification code.
     */
    public static final int VERIFICATION_CODE_LENGTH = 6;

    /**
     * The prefix of redis key for register.
     */
    public static final String REDIS_REGISTER_PREFIX = SERVICE_NAME + ":register:";

    /**
     * The prefix of redis key for login.
     */
    public static final String REDIS_LOGIN_PREFIX = SERVICE_NAME + ":login:";
}
