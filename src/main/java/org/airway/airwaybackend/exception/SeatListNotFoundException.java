package org.airway.airwaybackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class SeatListNotFoundException extends RuntimeException{
    public SeatListNotFoundException(String message) {
        super(message);
    }
}
