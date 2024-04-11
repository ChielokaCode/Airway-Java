package org.airway.airwaybackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingFlight {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "flight_no")
    private Flight flight;
    @OneToOne
    private PNR pnr;

    @ManyToOne
    private Classes classes;
    private String baggageAllowance;
    private BigDecimal taxFee;
    private BigDecimal surchargeFee;
    private BigDecimal serviceCharge;
    private BigDecimal baseFare;
    private BigDecimal totalFare;
    @JsonIgnore
    @ManyToOne
    private Booking booking;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Objects.hash(id);
        return result;
    }
}
