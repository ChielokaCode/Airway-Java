package org.airway.airwaybackend.controller;

import org.airway.airwaybackend.dto.PassengerDTo;
import org.airway.airwaybackend.model.Passenger;
import org.airway.airwaybackend.service.PassengerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/passenger")
public class PassengerController {
   private final PassengerService passengerService;

    public PassengerController(PassengerService passengerService) {
        this.passengerService = passengerService;
    }

    @GetMapping("/get-passengers")
    public ResponseEntity<Set<PassengerDTo>> getUsers (){
        Set<PassengerDTo> passengers= passengerService.findAll();
        return  ResponseEntity.ok(passengers);
    }
}
