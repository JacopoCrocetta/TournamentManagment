package com.tournamentmanagmentsystem.exception;

import org.springframework.http.*;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

        @Override
        @NonNull
        protected ResponseEntity<Object> handleMethodArgumentNotValid(
                        @NonNull MethodArgumentNotValidException ex,
                        @NonNull HttpHeaders headers,
                        @NonNull HttpStatusCode status,
                        @NonNull WebRequest request) {
                Map<String, String> errors = new HashMap<>();
                ex.getBindingResult().getFieldErrors()
                                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

                ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                                HttpStatus.BAD_REQUEST,
                                "Validation failed for one or more fields.");
                problemDetail.setTitle("Constraint Violation");
                problemDetail.setType(URI.create("https://api.tournamentmanagement.com/errors/validation-failed"));
                problemDetail.setProperty("errors", errors);

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
        }

        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<ProblemDetail> handleRuntimeException(RuntimeException ex) {
                log.error("Unhandled RuntimeException occurred: ", ex);
                ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                ex.getMessage());
                problemDetail.setTitle("Internal Server Error");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<ProblemDetail> handleGeneralException(Exception ex) {
                log.error("Unexpected Exception occurred: ", ex);
                ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                "An unexpected error occurred.");
                problemDetail.setTitle("Server Error");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail);
        }
}
