package org.airway.airwaybackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassengerConfirmationDto {
    private String title;
    private String firstName;
    private String lastName;
    private String ticketNo;
    private String seatNo;
    private String baggageAllowance;
}
