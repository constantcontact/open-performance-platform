package com.opp.exception;

/**
 * Thrown when an internal error occurs that should result in a 5XX response code.
 *
 * Created by ctobe on 6/24/16.
 */
public class InternalServiceException extends RuntimeException {

    public InternalServiceException(String message) {
        super(message);
    }

    public InternalServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
