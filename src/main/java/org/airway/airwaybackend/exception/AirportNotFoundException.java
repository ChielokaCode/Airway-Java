package org.airway.airwaybackend.exception;

import org.springframework.data.rest.webmvc.ResourceNotFoundException;

import java.io.Serial;

public class AirportNotFoundException extends ResourceNotFoundException {

    @Serial
    private static final long serialVersionUID = -4185306016942664972L;

    public AirportNotFoundException(String airportId) {
        super("Airport not found with id: " + airportId);
    }

}