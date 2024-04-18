package org.airway.airwaybackend.controller;

import org.airway.airwaybackend.dto.*;
import org.airway.airwaybackend.exception.*;
import org.airway.airwaybackend.enums.FlightDirection;
import org.airway.airwaybackend.dto.AddFlightDto;
import org.airway.airwaybackend.exception.AirlineNotFoundException;
import org.airway.airwaybackend.exception.AirportNotFoundException;
import org.airway.airwaybackend.model.Airport;
import org.airway.airwaybackend.model.Flight;
import org.airway.airwaybackend.serviceImpl.FlightServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/flights")
@CrossOrigin(origins = {"http://localhost:5173", "https://airway-ng.netlify.app"}, allowCredentials = "true")
public class FlightController {
    private final FlightServiceImpl flightService;

    @Autowired
    public FlightController(FlightServiceImpl flightService) {
        this.flightService = flightService;
    }

    @DeleteMapping("/delete-flight/{Id}")
    public ResponseEntity<String> deleteFlight(@PathVariable Long Id) {
        String response = flightService.deleteFlight(Id);
        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping("/availableFlight")
    public ResponseEntity<Map<String, FlightSearchResponse>> getAvailableFlight (
            @RequestParam (required = false, name = "departurePort") Airport departurePort,
            @RequestParam (required = false, name = "arrivalPort") Airport arrivalPort,
            @RequestParam (required = false, name = "departureDate") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate departureDate,
            @RequestParam (required = false, name = "flightDirection") FlightDirection flightDirection,
            @RequestParam (required = false, name = "returnDate") @DateTimeFormat(iso=DateTimeFormat.ISO.DATE) LocalDate returnDate

    ){
        Map<String, FlightSearchResponse> availableFlight = flightService.searchAvailableFlight(departurePort,arrivalPort,departureDate,flightDirection,returnDate);
        return ResponseEntity.ok(availableFlight);
    }


    @GetMapping("/departing-flights")
    public ResponseEntity<FlightSearchResponse> getDepartingFlights(
            @RequestParam(value = "departurePort", required = false) Airport departurePort,
            @RequestParam(value = "arrivalPort", required = false) Airport arrivalPort,
            @RequestParam(value = "departureDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate departureDate
            ) {

        FlightSearchResponse departingFlights = flightService.getDepartingFlights( departurePort, arrivalPort, departureDate);

        return ResponseEntity.ok(departingFlights);

    }

    @GetMapping("/returning-flights")
    public ResponseEntity<FlightSearchResponse> getReturningFlights(
            @RequestParam(value = "departurePort", required = false) Airport departurePort,
            @RequestParam(value = "arrivalPort", required = false) Airport arrivalPort,
            @RequestParam(value = "returnDate", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate returnDate
            ) {

        FlightSearchResponse returningFlights = flightService.getReturningFlights(departurePort, arrivalPort, returnDate);

        return ResponseEntity.ok(returningFlights);
    }

    @GetMapping("/all-returning-flights")
    public ResponseEntity<List<FlightSearchDto>> getAllReturningFlights(
            @RequestParam(value = "departurePort", required = false) Airport departurePort,
            @RequestParam(value = "arrivalPort", required = false) Airport arrivalPort
    ){
        List<FlightSearchDto> returningFlights = flightService.getAllReturningFlights(departurePort,arrivalPort);

        return ResponseEntity.ok(returningFlights);
    }

    @GetMapping("/all-departing-flights")
    public ResponseEntity<List<FlightSearchDto>> getAllDepartingFlights(
            @RequestParam(value = "departurePort", required = false) Airport departurePort,
            @RequestParam(value = "arrivalPort", required = false) Airport arrivalPort
    ){
        List<FlightSearchDto> departingFlights= flightService.getAllDepartingFlights(departurePort,arrivalPort);
        return ResponseEntity.ok(departingFlights);
    }

    @GetMapping("/fetch-all-flights")
    public ResponseEntity<Page<Flight>> getAllFlights(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize
    ) {

        return new ResponseEntity<>(flightService.getAllFlights(pageNo, pageSize), HttpStatus.OK);
    }
    @PostMapping("/add-flight")
    public ResponseEntity<String> addFlight(@RequestBody AddFlightDto flightDto) throws AirportNotFoundException, AirlineNotFoundException {
        String response = flightService.addNewFlight(flightDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/update-flight/{flightId}")
    public ResponseEntity<?> updateFlight(@PathVariable Long flightId, @RequestBody UpdateFlightDto updateFlightDto) {
        try {
            String response = flightService.updateFlight(flightId, updateFlightDto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (FlightNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
  @PostMapping("/confirm/{Id}")
    public ResponseEntity<String> confirmFlight(@PathVariable Long Id) {
        String response = flightService.confirmFlight(Id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/{Id}")
    public ResponseEntity<FlightSearchDto> getFlightDetails(@PathVariable Long Id) {
        return new ResponseEntity<>(flightService.getFlightDetails(Id), HttpStatus.OK);
    }

}

