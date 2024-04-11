package org.airway.airwaybackend.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.airway.airwaybackend.enums.BookingStatus;
import org.airway.airwaybackend.enums.FlightDirection;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String bookingReferenceCode;
    private FlightDirection tripType;
    private String passengerCode;
    @JsonIgnore
    @OneToMany
    private List<PNR> pnrList;
    @JsonIgnore
    @OneToMany
    private List<Ticket> tickets;
    @JsonIgnore
    @OneToMany
    private List<Passenger> passengers;
    @OneToMany(mappedBy = "booking", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    List<BookingFlight>bookingFlights;
    private BigDecimal totalFare;
    @JsonIgnore
    @ManyToOne
    private User userId;
    private Boolean pay;
    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;
    @CreationTimestamp
    private LocalDateTime createdAt;
    private String baggageAllowance;
    private BigDecimal taxFee;
    private BigDecimal surchargeFee;
    private BigDecimal serviceCharge;
    private BigDecimal baseFare;
    private String passengerContactEmail;


}
