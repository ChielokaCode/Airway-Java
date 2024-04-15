package org.chielokacode.airwaycc.airwaybackendcc.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private Booking booking;
    private String channel;
    private double amount;
    private LocalDateTime paymentDate;
    private String title;
    private String firstName;
    private String lastName;
    private String country;
    private String state;
    private String city;
    private String address;
}
