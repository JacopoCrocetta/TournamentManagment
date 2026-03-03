package com.tournamentmanagmentsystem.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a requested resource is not found (404).
 */
public class ResourceNotFoundException extends BaseException {
    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
