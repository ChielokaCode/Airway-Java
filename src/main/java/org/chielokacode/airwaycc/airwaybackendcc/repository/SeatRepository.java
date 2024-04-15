package org.chielokacode.airwaycc.airwaybackendcc.repository;

import org.chielokacode.airwaycc.airwaybackendcc.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatRepository extends JpaRepository<Seat, Long> {
}
