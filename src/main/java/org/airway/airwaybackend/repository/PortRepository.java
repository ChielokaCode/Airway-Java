package org.airway.airwaybackend.repository;

import org.airway.airwaybackend.model.Port;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PortRepository extends JpaRepository<Port, Long> {
}
