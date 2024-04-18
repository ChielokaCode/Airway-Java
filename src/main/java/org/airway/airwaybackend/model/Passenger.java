package org.airway.airwaybackend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.airway.airwaybackend.enums.Category;
import org.airway.airwaybackend.enums.Gender;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Passenger {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String passengerCode;
    private String firstName;
    private String lastName;
    private Gender gender;
    private String passengerEmail;
    private String title;
    private String nationality;
    private String phoneNumber;
    @OneToMany
    private List<Ticket> tickets;
    private String PSN;
    @Enumerated(EnumType.STRING)
    private Category category;
    @JsonIgnore
    @OneToOne
    private User user;
    private String contactPhone;
    private String contactEmail;
    @JsonIgnore
    @ManyToMany(mappedBy = "passengers")
    private List<Flight> flights;
    @OneToMany
    private  List<SeatList> seat;
    private Boolean contact;
    private LocalDate dateOfBirth;
    @JsonIgnore
    @ManyToOne
    private Booking bookings;
    private String bookingReference;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Objects.hash(id);
        return result;
    }
}