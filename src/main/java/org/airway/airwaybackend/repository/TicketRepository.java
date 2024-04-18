package org.airway.airwaybackend.repository;

import org.airway.airwaybackend.model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TicketRepository extends JpaRepository<Ticket, Long> {
}
