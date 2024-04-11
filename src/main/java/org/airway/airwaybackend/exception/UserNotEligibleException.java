package org.airway.airwaybackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)

public class UserNotEligibleException extends RuntimeException{
    public UserNotEligibleException(String message) {
        super(message);
    }
}
