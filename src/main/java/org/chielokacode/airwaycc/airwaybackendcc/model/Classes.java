package org.chielokacode.airwaycc.airwaybackendcc.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.chielokacode.airwaycc.airwaybackendcc.enums.FlightStatus;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Classes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String className;
    private double price;
    private double baggageAllowance;
    @Enumerated(EnumType.STRING)
    private FlightStatus flightStatus;
    private double taxFee;
    private double surchargeFee;
    private double serviceCharge;
    @OneToOne
    private Seat seat;
    @ManyToOne
    private Flight flight;
    @ManyToMany
    private List<Passenger> passengers;
    // Getters and setters
}
