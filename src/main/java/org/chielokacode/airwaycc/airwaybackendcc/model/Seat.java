package org.chielokacode.airwaycc.airwaybackendcc.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Seat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Classes className;
    private char seatAlphabet;
    private int numberOfSeat;
    @ManyToOne
    private Flight flightName;
    // Getters and setters
}
