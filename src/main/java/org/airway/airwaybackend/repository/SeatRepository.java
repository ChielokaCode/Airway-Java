package org.airway.airwaybackend.repository;

import org.airway.airwaybackend.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {
}
