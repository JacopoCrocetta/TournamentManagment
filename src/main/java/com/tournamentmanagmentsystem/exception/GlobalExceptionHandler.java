package com.tournamentmanagmentsystem.exception;

import org.springframework.http.*;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.URI;
import lombok.extern.slf4j.Slf4j;
import java.util.Objects;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
        @Override
        protected ResponseEntity<Object> handleMethodArgumentNotValid(
                        @org.springframework.lang.NonNull MethodArgumentNotValidException ex,
                        @org.springframework.lang.NonNull HttpHeaders headers,
                        @org.springframework.lang.NonNull HttpStatusCode status,
                        @org.springframework.lang.NonNull WebRequest request) {
                Map<String, String> errors = new HashMap<>();
                ex.getBindingResult().getFieldErrors()
                                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

                ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                                HttpStatus.BAD_REQUEST,
                                "Validation failed for one or more fields.");
                problemDetail.setTitle("Constraint Violation");
                problemDetail.setType(Objects.requireNonNull(URI.create("https://api.tournamentmanagement.com/errors/validation-failed")));
                problemDetail.setProperty("errors", errors);

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
        }

        @ExceptionHandler(NotFoundException.class)
        public ResponseEntity<ProblemDetail> handleNotFoundException(NotFoundException ex) {
                ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
                problemDetail.setTitle("Resource Not Found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(problemDetail);
        }

        @ExceptionHandler(BusinessRuleViolationException.class)
        public ResponseEntity<ProblemDetail> handleBusinessRuleViolationException(BusinessRuleViolationException ex) {
                ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
                problemDetail.setTitle("Business Rule Violation");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(problemDetail);
        }

        @ExceptionHandler(InvalidStateTransitionException.class)
        public ResponseEntity<ProblemDetail> handleInvalidStateTransitionException(InvalidStateTransitionException ex) {
                ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
                problemDetail.setTitle("Invalid State Transition");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(problemDetail);
        }

        @ExceptionHandler(ConflictException.class)
        public ResponseEntity<ProblemDetail> handleConflictException(ConflictException ex) {
                ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
                problemDetail.setTitle("Conflict");
                return ResponseEntity.status(HttpStatus.CONFLICT).body(problemDetail);
        }

        @ExceptionHandler(AccessDeniedBusinessException.class)
        public ResponseEntity<ProblemDetail> handleAccessDeniedBusinessException(AccessDeniedBusinessException ex) {
                ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.FORBIDDEN, ex.getMessage());
                problemDetail.setTitle("Access Denied");
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(problemDetail);
        }

        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<ProblemDetail> handleRuntimeException(RuntimeException ex) {
                log.error("Unhandled RuntimeException occurred: ", ex);
                ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(
                                HttpStatus.INTERNAL_SERVER_ERROR,
                                "An unexpected error occurred.");
                problemDetail.setTitle("Internal Server Error");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(problemDetail);
        }

        @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<ProblemDetail> handleDataIntegrityViolation(org.springframework.dao.DataIntegrityViolationException ex, WebRequest request) {
        System.err.println("CRITICAL: Data Integrity Violation: " + ex.getMessage());
        Throwable rootCause = ex.getRootCause();
        if (rootCause != null) {
            System.err.println("ROOT CAUSE: " + rootCause.getMessage());
        }
        ex.printStackTrace();
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, "Data integrity violation occurred.");
        problemDetail.setTitle("Data Integrity Violation");
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
