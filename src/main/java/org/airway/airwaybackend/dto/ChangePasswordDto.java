package org.airway.airwaybackend.dto;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordDto {
private String email;
@Pattern(regexp = "^.*(?=.{8,})(?=...*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$", message = "*Enter at least one uppercase,lowercase,digit and special character and minimum 8 characters")
private String oldPassword;
@Pattern(regexp = "^.*(?=.{8,})(?=...*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$", message = "*Enter at least one uppercase,lowercase,digit and special character and minimum 8 characters")
private String newPassword;
}
