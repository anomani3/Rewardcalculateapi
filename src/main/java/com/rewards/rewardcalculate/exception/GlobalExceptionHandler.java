package com.rewards.rewardcalculate.exception;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.time.LocalDateTime;
import java.util.*;

/**
 * GlobalExceptionHandler is a centralized exception handler using @RestControllerAdvice.
 * It catches and handles exceptions thrown from anywhere in the application,
 * providing meaningful HTTP responses and error messages.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Handles NoSuchElementException, typically thrown when a requested
     * resource like a customer ID is not found in the database or service layer.
     *
     * @param ex the exception thrown
     * @return ResponseEntity with HTTP 404 Not Found and a custom error message
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNotFound(NoSuchElementException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body("Customer not found: " + ex.getMessage());
    }

    /**
     * Handles IllegalArgumentException, commonly thrown when an input value
     * violates business logic or application rules.
     *
     * @param ex the exception thrown
     * @return ResponseEntity with HTTP 400 Bad Request and a custom error message
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleBadRequest(IllegalArgumentException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Invalid input: " + ex.getMessage());
    }


}
