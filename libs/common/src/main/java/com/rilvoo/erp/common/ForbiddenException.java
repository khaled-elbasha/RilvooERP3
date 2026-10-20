package com.rilvoo.erp.common;

/** Authenticated principal lacks the required effective permission. Maps to HTTP 403. */
public class ForbiddenException extends ErpException {

    public ForbiddenException(String message) {
        super("ACCESS_DENIED", message);
    }
}
