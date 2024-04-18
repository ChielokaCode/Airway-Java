package org.airway.airwaybackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private Booking booking;
    private String paymentReference;
    private String transactionReference;

    private String email;
    private String channel = "PAYSTACK";
    private double amount;
    private LocalDateTime paymentDate;
    private String callBackUrl;
    private String transactionStatus;
    private String currency;
    private Boolean isVerified= false;
    private String recipientCode;
    private String title;
    private String firstName;
    private String lastName;
    private String country;
    private String state;
    private String city;
    private String address;
    @ManyToOne(fetch = FetchType.EAGER)
    private User user;
}
