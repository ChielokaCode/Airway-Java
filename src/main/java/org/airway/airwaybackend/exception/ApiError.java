package org.airway.airwaybackend.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record ApiError(
        String path,
        String message,
        int statusCode,
        LocalDateTime localDateTime,
        List<ValidationError> errors
) {
}
