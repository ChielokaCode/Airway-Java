package org.airway.airwaybackend.controller;

import org.airway.airwaybackend.model.Airport;
import org.airway.airwaybackend.service.AirportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/airports")
@CrossOrigin(origins = {"http://localhost:5173", "https://airway-ng.netlify.app"}, allowCredentials = "true")
public class AirportController {

    @Autowired
    private AirportService airportService;

    private static final Logger logger = LoggerFactory.getLogger(AirportController.class);

    @GetMapping("/all-airports")
    public @ResponseBody List<Airport> getAllAirports() {
        return airportService.getAllAirports();
    }

    @GetMapping("/{iata-code}")
    public @ResponseBody Airport getAirportById(@PathVariable("iata-code") String iataCode) {
        return airportService.getAirportById(iataCode);
    }




}
