package org.airway.airwaybackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JsonIgnore
    @ManyToOne
    private BookingFlight bookingFlight;
    @JsonIgnore
    @ManyToOne
    private Passenger passenger;
    @JsonIgnore
    @ManyToOne
    private Booking booking;
    private String ticketNo;
}
