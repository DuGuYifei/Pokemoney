package com.pokemoney.commons.errors;

/**
 * The abstract class HttpBaseError represents a base class for all HTTP error
 * types that can occur within the system.
 * It provides the status code and the error message as well.
 */
public abstract class HttpBaseError extends Exception {

    /**
     * The status code of the error.
     */
    public int statusCode;

    /**
     * The exception that caused the error.
     */
    public Exception e;

    /**
     * Constructs an HttpBaseError with the given status code and error.
     *
     * @param statusCode the status code of the error
     * @param error      the exception that caused the error
     */
    protected HttpBaseError(int statusCode, Exception error) {
        this.statusCode = statusCode;
        this.e = error;
    }

    /**
     * Constructs an HttpBaseError with the given status code and message.
     *
     * @param statusCode the status code of the error
     * @param message    the error message
     */
    protected HttpBaseError(int statusCode, String message) {
        this.statusCode = statusCode;
        this.e = new Exception(message);
    }
}