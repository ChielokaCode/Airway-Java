package org.airway.airwaybackend.controller;

import org.airway.airwaybackend.dto.FlightSearchDto;
import org.airway.airwaybackend.dto.FlightSearchResponse;
import org.airway.airwaybackend.enums.FlightDirection;
import org.airway.airwaybackend.model.Airport;
import org.airway.airwaybackend.model.Flight;
import org.airway.airwaybackend.serviceImpl.FlightServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class FlightControllerTest {
    @Mock
    private FlightServiceImpl flightServiceImpl;

    @InjectMocks
    private FlightController flightController;

    public FlightControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAvailableFlight() {
        Airport departurePort = new Airport();
        Airport arrivalPort = new Airport();
        LocalDate departureDate = LocalDate.now();
        LocalDate arrivalDate = LocalDate.now().plusDays(1);
        LocalDate returnDate = LocalDate.of(2024,3,2);
        FlightDirection flightDirection = FlightDirection.ONE_WAY;
        FlightDirection flightDirection1 = FlightDirection.ROUND_TRIP;
        int noOfAdult = 2;
        int noOfChildren = 1;
        int noOfInfant = 0;
        Flight flight1 = new Flight();
        Flight flight2 = new Flight();
        List<FlightSearchDto> flightSearchDtos = new ArrayList<>();
        FlightSearchDto flightDTO1 = new FlightSearchDto();
        FlightSearchDto flightDTO2 = new FlightSearchDto();
        flightSearchDtos.add(flightDTO1);
        flightSearchDtos.add(flightDTO2);
        FlightSearchResponse flightSearchResponse = new FlightSearchResponse();
        flightSearchResponse.setFlights(flightSearchDtos);
        Map<String, FlightSearchResponse> flightSearchResponseMap = new HashMap<>();
        flightSearchResponseMap.put("departing flight", flightSearchResponse);


        flightSearchResponse.setTotalFlights(flightSearchDtos.size());
        when(flightServiceImpl.searchAvailableFlight(departurePort, arrivalPort, departureDate,flightDirection ,returnDate))
                .thenReturn(flightSearchResponseMap);

        ResponseEntity< Map<String, FlightSearchResponse>> responseEntity = flightController.getAvailableFlight(departurePort, arrivalPort, departureDate, flightDirection1,returnDate);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}