package org.chielokacode.airwaycc.airwaybackendcc.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.chielokacode.airwaycc.airwaybackendcc.enums.BookingStatus;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String PNR;
    private String ticketNo;
    @ManyToMany
    private List<Passenger> passengers;
    @ManyToMany
    private List<Flight> flights;
    @ManyToMany
    private List<Seat> seats;
    private double amount;
    @ManyToOne
    private User userId;
    private Boolean pay;
    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;
    // Getters and setters
}
