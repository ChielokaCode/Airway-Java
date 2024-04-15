package org.chielokacode.airwaycc.airwaybackendcc.repository;

import org.chielokacode.airwaycc.airwaybackendcc.model.Aircraft;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AircraftRepository extends JpaRepository<Aircraft, Long> {
}
