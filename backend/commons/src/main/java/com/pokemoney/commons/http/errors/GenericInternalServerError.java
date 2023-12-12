package com.pokemoney.commons.http.errors;

/**
 * An error indicating that the server encountered an unexpected condition
 * which prevented it from fulfilling the request.
 * This error extends the HttpBaseError class and has a status code of 500
 * (Internal Server Error).
 */
public class GenericInternalServerError extends HttpBaseError {
        /**
        * Constructs a new GenericInternalServerError with the specified detail message.
        *
        * @param message the detail message.
        */
        public GenericInternalServerError(String message) {
            super(500, message);
        }

        /**
        * Constructs a new GenericInternalServerError with the specified cause.
        *
        * @param e the cause.
        */
        public GenericInternalServerError(Exception e) {
            super(500, e);
        }
}
