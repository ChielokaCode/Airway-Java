package org.chielokacode.airwaycc.airwaybackendcc.repository;

import org.chielokacode.airwaycc.airwaybackendcc.model.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerRepository extends JpaRepository<Passenger, Long> {
}
