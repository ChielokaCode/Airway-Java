package org.airway.airwaybackend.service;

import org.airway.airwaybackend.dto.PassengerDTo;

import java.util.Set;

public interface PassengerService {
     Set<PassengerDTo> findAll();
}
