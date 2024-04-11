package org.airway.airwaybackend.repository;

import org.airway.airwaybackend.model.BookingFlight;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingFlightRepository extends JpaRepository<BookingFlight, Long> {
}
