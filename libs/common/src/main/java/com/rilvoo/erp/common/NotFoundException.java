package com.rilvoo.erp.common;

/** A referenced aggregate does not exist (or is soft-deleted). Maps to HTTP 404. */
public class NotFoundException extends ErpException {

    public NotFoundException(String resourceName, Object id) {
        super("NOT_FOUND", resourceName + " '" + id + "' was not found");
    }

    public NotFoundException(String message) {
        super("NOT_FOUND", message);
    }
}
