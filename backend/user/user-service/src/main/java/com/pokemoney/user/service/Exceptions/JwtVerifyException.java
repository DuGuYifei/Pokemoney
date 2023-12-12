package com.pokemoney.user.service.Exceptions;

/**
 * This exception is thrown when the JWT token is invalid.
 */
public class JwtVerifyException extends Exception {
    public JwtVerifyException(String message) {
        super(message);
    }

    /**
     * This exception is thrown when the user is invalid for the token.
     */
    public static class InvalidUserException extends JwtVerifyException {
        public InvalidUserException() {
            super("Invalid user for this token");
        }
    }

    /**
     * This exception is thrown when the token is invalid.
     */
    public static class InvalidTokenException extends JwtVerifyException {
        public InvalidTokenException() {
            super("Invalid token");
        }
    }
}
