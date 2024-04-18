package org.airway.airwaybackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Seat {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonIgnore
    @OneToOne
    private Classes className;
    private char seatAlphabet;
    private int totalNumberOfSeat;
    @JsonIgnore
    @ManyToOne
    private Flight flightName;
    private int availableSeat;
    private int noOfOccupiedSeats;
    @JsonIgnore
    @OneToMany(cascade = CascadeType.REMOVE)
    private List<SeatList> seatLists;

}


