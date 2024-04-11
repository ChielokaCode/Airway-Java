package org.airway.airwaybackend.serviceImpl;


import jakarta.transaction.Transactional;
import org.airway.airwaybackend.exception.AirportNotFoundException;
import org.airway.airwaybackend.model.Airport;
import org.airway.airwaybackend.repository.AirportRepository;
import org.airway.airwaybackend.service.AirportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional()
public class AirportServiceImpl implements AirportService {

    @Autowired
    private AirportRepository airportRepo;

    @Override
    public Airport getAirportById(String airportId) {
        return airportRepo.findById(airportId).orElseThrow(() -> new AirportNotFoundException(airportId));
    }

    @Override
    public List<Airport> getAllAirports() {
        return airportRepo.findAll();
    }

}