package com.opp.exception;

/**
 * Created by ctobe on 6/24/16.
 */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
