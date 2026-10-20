package com.rilvoo.example.common;

/**
 * A uniqueness/state conflict against existing server-side data (e.g. a database unique
 * constraint violation such as SQLSTATE 23505). Maps to HTTP 409. The message must never
 * contain raw SQL, driver details or rejected secret values.
 */
public class ResourceConflictException extends ApplicationException {

    public ResourceConflictException(String message) {
        super("CONFLICT", message);
    }

    public ResourceConflictException(String code, String message) {
        super(code, message);
    }
}
