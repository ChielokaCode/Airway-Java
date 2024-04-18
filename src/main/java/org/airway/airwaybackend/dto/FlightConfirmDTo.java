package org.airway.airwaybackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class FlightConfirmDTo {
    private String flightNo;
    private String className;
    private String departurePortIata;
    private String arrivalPortIata;
    private String departurePortCity;
    private String arrivalPortCity;
    private String departureDate;
    private String arrivalDate ;
    private String departureTime;
    private String arrivalTime ;
    private String bagageAllowance;
    private List<PassengerConfirmationDto> passengerList;


}
