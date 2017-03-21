package com.opp.exception;

/**
 * Exception should be thrown if a requested resource is not found.
 *
 * Created by ctobe on 7/13/16.
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException() {
        super();
    }

    public ResourceNotFoundException(String message){
        super(message);
    }
}
