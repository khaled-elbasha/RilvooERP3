package com.rilvoo.core.common;

/**
 * The authenticated principal is known but lacks the effective permission required by the
 * operation. Maps to HTTP 403. Never include the missing permission list in client output if
 * it would disclose authorization structure; keep that server-side.
 */
public class ForbiddenException extends ApplicationException {

    public ForbiddenException(String message) {
        super("ACCESS_DENIED", message);
    }

    public ForbiddenException(String code, String message) {
        super(code, message);
    }
}
