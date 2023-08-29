package com.pokemoney.userservice.errors;

/**
 * An error indicating that the client does not have permission to access the
 * requested resource.
 * This error extends the HttpBaseError class and has a status code of 403
 * (Forbidden).
 */
public class GenericForbiddenError extends HttpBaseError {

    /**
     * Constructs a new GenericForbiddenError with the specified detail message.
     *
     * @param message the detail message.
     */
    public GenericForbiddenError(String message) {
        super(403, message);
    }

    /**
     * Constructs a new GenericForbiddenError with the specified cause.
     *
     * @param e the cause.
     */
    public GenericForbiddenError(Exception e) {
        super(403, e);
    }
}