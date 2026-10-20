package com.rilvoo.example.web;

import com.rilvoo.example.apicontract.ApiError;
import com.rilvoo.example.apicontract.ApiMeta;
import com.rilvoo.example.apicontract.ApiResponse;
import com.rilvoo.example.common.ApplicationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

/**
 * Global REST exception handling producing the uniform envelope with correct status codes.
 * Never exposes stack traces, SQL, driver messages or rejected secret values. Unexpected
 * exceptions are logged server-side (with the request id) but surface as a generic 500.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /** 404 — referenced resource missing or invisible. */
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ApiResponse<Void>> handleApplication(ApplicationException ex) {
        HttpStatus status = statusFor(ex.errorCode());
        return envelope(status, ApiError.of(ex.errorCode(), ex.getMessage()));
    }

    /** 400 — bean validation failure on a @Valid request body (nested paths preserved). */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleBodyValidation(MethodArgumentNotValidException ex) {
        return envelope(HttpStatus.BAD_REQUEST,
                ApiError.validation("Request validation failed", collectFieldErrors(ex.getBindingResult().getFieldErrors())));
    }

    /** 400 — form/query binding validation failure. */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiResponse<Void>> handleBind(BindException ex) {
        return envelope(HttpStatus.BAD_REQUEST,
                ApiError.validation("Request validation failed", collectFieldErrors(ex.getBindingResult().getFieldErrors())));
    }

    /** 400 — Jakarta Validator constraint violation on method parameters. */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraint(ConstraintViolationException ex) {
        Map<String, List<String>> fields = new LinkedHashMap<>();
        for (ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            // Property path last two segments keep nesting readable: address.postalCode -> postalCode path kept fully.
            String path = violation.getPropertyPath().toString();
            fields.computeIfAbsent(shorten(path), k -> new ArrayList<>()).add(violation.getMessage());
        }
        return envelope(HttpStatus.BAD_REQUEST, ApiError.validation("Request validation failed", fields));
    }

    /** 400 — malformed identifiers etc. (message is already sanitized by RequestIds). */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgument(IllegalArgumentException ex) {
        return envelope(HttpStatus.BAD_REQUEST, ApiError.of("BAD_REQUEST", safeMessage(ex)));
    }

    /** Honours explicit ResponseStatusException raised anywhere in application code. */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiResponse<Void>> handleResponseStatus(ResponseStatusException ex) {
        return envelope(HttpStatus.valueOf(ex.getStatusCode().value()),
                ApiError.of(ex.getStatusCode().value() == 404 ? "NOT_FOUND" : "REQUEST_REJECTED", safeMessage(ex)));
    }

    /** 500 — catch-all. Log detail stays server-side; the client sees a generic envelope. */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleUnexpected(Exception ex) {
        org.slf4j.LoggerFactory.getLogger(GlobalExceptionHandler.class)
                .error("Unhandled exception requestId={}", MDC.get(CorrelationKeys.MDC_REQUEST_ID), ex);
        return envelope(HttpStatus.INTERNAL_SERVER_ERROR, ApiError.internal());
    }

    private static HttpStatus statusFor(String errorCode) {
        return switch (errorCode) {
            case "NOT_FOUND" -> HttpStatus.NOT_FOUND;
            case "CONFLICT" -> HttpStatus.CONFLICT;
            case "ACCESS_DENIED" -> HttpStatus.FORBIDDEN;
            case "BUSINESS_RULE_VIOLATION" -> HttpStatus.UNPROCESSABLE_ENTITY;
            default -> HttpStatus.BAD_REQUEST;
        };
    }

    private static Map<String, List<String>> collectFieldErrors(List<FieldError> fieldErrors) {
        Map<String, List<String>> fields = new LinkedHashMap<>();
        for (FieldError error : fieldErrors) {
            // FieldError#getField keeps nested paths like "address.postalCode".
            fields.computeIfAbsent(error.getField(), k -> new ArrayList<>())
                    .add(error.getDefaultMessage() == null ? "invalid value" : error.getDefaultMessage());
        }
        return fields;
    }

    private static String shorten(String propertyPath) {
        int idx = propertyPath.lastIndexOf('(');
        return idx >= 0 ? propertyPath.substring(idx + 1, Math.max(idx + 1, propertyPath.length() - 1)) : propertyPath;
    }

    private static String safeMessage(Throwable ex) {
        String message = ex.getMessage();
        return message == null || message.isBlank() ? "The request could not be processed" : message;
    }

    private static ResponseEntity<ApiResponse<Void>> envelope(HttpStatus status, ApiError error) {
        ApiResponse<Void> body = ApiResponse.failure(error,
                ApiMeta.of(MDC.get(CorrelationKeys.MDC_REQUEST_ID), Instant.now()));
        return ResponseEntity.status(status).body(body);
    }
}
