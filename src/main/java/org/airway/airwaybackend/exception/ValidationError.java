package org.airway.airwaybackend.exception;

public record ValidationError(
        String field,
        String message
) {
}