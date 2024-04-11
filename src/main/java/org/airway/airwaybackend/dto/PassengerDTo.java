package org.airway.airwaybackend.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.airway.airwaybackend.enums.Category;
import org.airway.airwaybackend.enums.Gender;
import org.airway.airwaybackend.enums.Role;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassengerDTo {
    @Size(min = 3, message = "First name must be at least 3 characters")
    private String firstName;
    @Size(min = 3, message = "Last name must be at least 3 characters")
    private String lastName;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private String passengerEmail;
    private String title;
    private String Nationality;
    private String membership;
    private LocalDate created;
    private String phoneNumber;
    @NotEmpty(message = "Role cannot be empty")
    @Enumerated(EnumType.STRING)
    private final Role userRole = Role.PASSENGER;
    private String passengerCode;
    @Enumerated(EnumType.STRING)
    private Category category;
    private String contactPhone;
    private String contactEmail;
    private Boolean contact;
    private LocalDate dateOfBirth;
}
