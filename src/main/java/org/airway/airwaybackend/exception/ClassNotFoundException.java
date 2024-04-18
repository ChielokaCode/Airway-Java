package org.airway.airwaybackend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ClassNotFoundException extends Exception{
    public ClassNotFoundException(String message) {
        super(message);
    }
}
