package com.neueda.portfolio_manager.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Not a @RestControllerAdvice on purpose: only specific endpoints should use
 * this handling, so it is called explicitly (via try/catch) from those
 * controller methods instead of applying globally to every endpoint.
 */
public final class GlobalExceptionHandler {

    private GlobalExceptionHandler() {
    }

    public static ResponseEntity<Map<String, Object>> handle(Exception ex) {
        if (ex instanceof IllegalArgumentException) {
            return build(HttpStatus.BAD_REQUEST, ex.getMessage());
        }
        if (ex instanceof IllegalStateException) {
            return build(HttpStatus.CONFLICT, ex.getMessage());
        }
        if (ex instanceof DataAccessException dae) {
            return build(HttpStatus.INTERNAL_SERVER_ERROR, "Database error: " + dae.getMostSpecificCause().getMessage());
        }
        return build(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error: " + ex.getMessage());
    }

    private static ResponseEntity<Map<String, Object>> build(HttpStatus status, String message) {
        Map<String, Object> body = new HashMap<>();
        body.put("status", status.value());
        body.put("error", status.getReasonPhrase());
        body.put("message", message);
        return ResponseEntity.status(status).body(body);
    }
}
