package org.airway.airwaybackend.repository;

import org.airway.airwaybackend.model.Aircraft;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AircraftRepository extends JpaRepository<Aircraft, Long> {
}
