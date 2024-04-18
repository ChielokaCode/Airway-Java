package org.airway.airwaybackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PNR {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    @JsonIgnore
    @OneToOne
    private BookingFlight bookingFlight;
    @ManyToMany
    private List<Passenger> passengerList;
    private String PNRCode;
    @JsonIgnore
    @ManyToOne
    private Classes classes;
}
