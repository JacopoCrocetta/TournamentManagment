package com.tournamentmanagmentsystem.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown when a request conflicts with current state (409).
 */
public class ConflictException extends BaseException {
    public ConflictException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}
