package org.airway.airwaybackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class SeatList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonIgnore
    @ManyToOne(cascade = CascadeType.REMOVE)
    private Seat seat;
    private String seatLabel;
    @JsonIgnore
    @ManyToOne
    private Passenger assignedPerson;
    private Boolean occupied;
}
