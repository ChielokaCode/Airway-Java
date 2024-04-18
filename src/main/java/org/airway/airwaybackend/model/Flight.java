package org.airway.airwaybackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.airway.airwaybackend.enums.FlightDirection;
import org.airway.airwaybackend.enums.FlightStatus;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Builder
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private FlightDirection flightDirection;
    private FlightStatus flightStatus;
    @Column(unique = true)
    private String flightNo;
    @ManyToOne
    private Airline airline;
    private LocalDate arrivalDate;
    private LocalDate departureDate;
    private LocalTime arrivalTime;
    private LocalTime departureTime;
    private long duration;
    @ManyToOne
    private Airport arrivalPort;
    @ManyToOne
    private Airport departurePort;
//    @JsonIgnore
//    check
    @OneToMany(mappedBy = "flight", cascade = CascadeType.REMOVE)
    private List<Classes> classes;
    private int totalSeat;
    private int availableSeat;
    @JsonIgnore
    @ManyToOne
    private User user;
    @JsonIgnore
    @ManyToMany
    @JoinTable(
            joinColumns = @JoinColumn(name = "flight_id"),
            inverseJoinColumns = @JoinColumn(name = "passenger_id")
    )
    private List<Passenger> passengers;

//    @Override
//    public int hashCode() {
//        final int prime = 31;
//        int result = 1;
//        result = prime * result + Objects.hash(id);
//        return result;
//    }
}