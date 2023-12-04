package com.pokemoney.hadoop.client.exception;

/**
 * Generic internal exception to graphql.
 */
public class GenericGraphQlInternalException extends Exception {
    /**
     * Constructor
     *
     * @param message message
     */
    public GenericGraphQlInternalException(String message) {
        super(message);
    }
}
