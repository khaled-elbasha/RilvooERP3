package com.rilvoo.core.common;

import java.util.Objects;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Safe identifier handling shared across layers: request IDs accepted from clients must be
 * validated and sanitized before they are echoed into responses, MDC or logs. Anything that
 * does not match the conservative pattern is replaced with a freshly generated UUID.
 */
public final class RequestIds {

    /** Up to 64 chars of [A-Za-z0-9._-]; rejects header injection characters outright. */
    private static final Pattern SAFE = Pattern.compile("^[A-Za-z0-9._-]{1,64}$");

    private RequestIds() {
    }

    public static boolean isSafe(String candidate) {
        return candidate != null && SAFE.matcher(candidate).matches();
    }

    /** Returns the candidate when safe, otherwise a new random UUID string. */
    public static String sanitizeOrGenerate(String candidate) {
        return isSafe(candidate) ? candidate : newId();
    }

    public static String newId() {
        return UUID.randomUUID().toString();
    }

    /** Parses a path variable into a UUID, failing with 400-shaped IllegalArgumentException. */
    public static UUID parseUuid(String value) {
        Objects.requireNonNull(value, "value");
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Invalid identifier: " + sanitizeForLog(value));
        }
    }

    /** Truncates untrusted text for log lines so hostile input cannot flood appenders. */
    public static String sanitizeForLog(String value) {
        if (value == null) {
            return "";
        }
        String cleaned = value.replaceAll("[\\r\\n\\t]", "_");
        return cleaned.length() <= 120 ? cleaned : cleaned.substring(0, 120) + "...";
    }
}
