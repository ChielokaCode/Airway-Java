package org.airway.airwaybackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Classes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String className;
    private String baggageAllowance;
    private BigDecimal taxFee;
    private BigDecimal surchargeFee;
    private BigDecimal serviceCharge;
    private BigDecimal baseFare;
    private BigDecimal totalFare;
    @OneToMany
    private List<PNR> pnrList;

    @OneToOne(cascade = CascadeType.REMOVE)
    private Seat seat;
    @JsonIgnore
    @ManyToOne
    private Flight flight;
    @JsonIgnore
    @OneToMany
    private List<Passenger> passengers;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Objects.hash(id);
        return result;
    }
}

