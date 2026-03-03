package com.tournamentmanagmentsystem.exception;

import org.springframework.http.HttpStatusCode;
import org.springframework.lang.NonNull;
import lombok.Getter;

/**
 * Base abstract exception for the application.
 * All custom domain exceptions should extend this.
 */
@Getter
public abstract class BaseException extends RuntimeException {
    @NonNull
    private final HttpStatusCode status;

    protected BaseException(String message, @NonNull HttpStatusCode status) {
        super(message);
        this.status = status;
    }
}
