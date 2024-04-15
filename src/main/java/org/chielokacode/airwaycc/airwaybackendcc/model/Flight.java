package org.chielokacode.airwaycc.airwaybackendcc.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.chielokacode.airwaycc.airwaybackendcc.enums.FlightDirection;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private FlightDirection flightDirection;
    private String flightNo;
    private String airline;
    private String airplaneName;
    private String arrivalDate;
    private String departureDate;
    private String arrivalPort;
    private String departurePort;
    @OneToMany(mappedBy = "flight")
    private List<Classes> classes;
    private int totalSeat;
    private int availableSeat;
    private String pilotName;
    private int noOfChildren;
    private int noOfAdult;
    @ManyToMany
    private List<Passenger> passengers;
    // Getters and setters
}
