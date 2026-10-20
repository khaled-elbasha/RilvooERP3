package com.rilvoo.example.common;

/** A referenced resource does not exist or is not visible to the caller. Maps to HTTP 404. */
public class ResourceNotFoundException extends ApplicationException {

    public ResourceNotFoundException(String resourceName, Object id) {
        super("NOT_FOUND", resourceName + " '" + id + "' was not found");
    }

    public ResourceNotFoundException(String message) {
        super("NOT_FOUND", message);
    }
}
