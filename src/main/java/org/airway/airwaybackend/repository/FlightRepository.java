package org.airway.airwaybackend.repository;

import org.airway.airwaybackend.model.Airport;
import org.airway.airwaybackend.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface FlightRepository extends JpaRepository<Flight, Long> {
   Optional<List<Flight>> searchByDeparturePortAndArrivalPortAndDepartureDate(Airport departurePort, Airport arrivalPort, LocalDate departureDate);
    Optional<List<Flight>> searchByDeparturePortAndArrivalPort(Airport departurePort, Airport arrivalPort);

}
