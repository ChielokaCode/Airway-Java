package org.airway.airwaybackend.dto;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class UserDto {
    private String firstName;
    private String lastName;
    private String email;
    private String country;
    private String phoneNumber;
    private String gender;
    private String dateOfBirth;

    public UserDto(String firstName, String lastName, String email, String country, String phoneNumber, String gender, String dateOfBirth) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.country = country;
        this.phoneNumber = phoneNumber;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
    }
}
