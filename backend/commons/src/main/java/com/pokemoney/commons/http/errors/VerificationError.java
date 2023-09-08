package com.pokemoney.commons.http.errors;

/**
 * The error throws when verify fails.
 */
public class VerificationError extends HttpBaseError {

    /**
     * Constructs a new VerificationError with the specified detail message.
     */
    public VerificationError(String message) {
        super(403, message);
    }
}
