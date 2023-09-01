package com.pokemoney.userservice;

public class Constants {
    // TODO: Change to @Value
    /**
     * The secret key for JWT.
     */
    public static final String JWT_SECRET = "pokemoney";
    /**
     * The subject for JWT.
     */
    public static final String JWT_SUBJECT = "user-service";

    /**
     * The key of leaf-service in eureka-service.
     */
    public static final String LEAF_IN_EUREKA_KEY = "leaf-service";

    /**
     * The key of user-service in leaf-service.
     */
    public static final String USER_IN_LEAF_KEY = "user-service";

    /**
     * The length of verification code.
     */
    public static final int VERIFICATION_CODE_LENGTH = 6;
}
