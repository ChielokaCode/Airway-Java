package org.chielokacode.airwaycc.airwaybackendcc.repository;

import org.chielokacode.airwaycc.airwaybackendcc.model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FlightRepository extends JpaRepository<Flight, Long> {
}
