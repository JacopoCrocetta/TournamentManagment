package com.tournamentmanagmentsystem.exception;

import org.springframework.http.HttpStatus;

/**
 * Exception thrown for business rule violations (400).
 */
public class BusinessException extends BaseException {
    public BusinessException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
