package org.airway.airwaybackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.airway.airwaybackend.enums.FlightDirection;
import org.airway.airwaybackend.enums.FlightStatus;
import org.airway.airwaybackend.model.Classes;
import org.airway.airwaybackend.model.Seat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddFlightDto {
    private Long id;
    private FlightDirection flightDirection;
    private String flightNo;
    private String airlineName;
    private LocalDate arrivalDate;
    private LocalDate departureDate;
    private LocalTime arrivalTime;
    private LocalTime departureTime;
    private long duration;
    private String arrivalPortName;
    private String departurePortName;
    private List<Classes> classes;
    private Set<Seat> seat;
    private Integer totalSeat;
    private FlightStatus flightStatus;
    private Integer availableSeat;

}
