package org.airway.airwaybackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingConfirmationDto {
    private List<FlightConfirmDTo> flightDetails;
    private String bookingRef;
    private List<PnrDto> PNRCodes;
    private List<PassengerConfirmationDto> passengerList;
    private String UserFullName;
    private String expiryDate;
    private String expiryTime;
}
