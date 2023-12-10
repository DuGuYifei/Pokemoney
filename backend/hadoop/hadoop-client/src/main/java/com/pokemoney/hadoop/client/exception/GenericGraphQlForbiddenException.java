package com.pokemoney.hadoop.client.exception;


/**
 * Generic forbidden exception.
 */
public class GenericGraphQlForbiddenException extends Exception {
    /**
     * Constructor
     *
     * @param message message
     */
    public GenericGraphQlForbiddenException(String message) {
        super(message);
    }
}
