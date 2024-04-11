package org.airway.airwaybackend.service;



import org.airway.airwaybackend.model.Airport;

import java.util.List;


public interface AirportService {
    Airport getAirportById(String airportId);
    List<Airport> getAllAirports();
}
