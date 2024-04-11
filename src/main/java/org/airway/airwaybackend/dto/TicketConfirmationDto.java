package org.airway.airwaybackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketConfirmationDto {
    private String bookingRef;
    private List<FlightConfirmDTo> flightDetails;
//    private String ticketRef;
    private List<PnrDto> PNRCode;
//    private List<PassengerConfirmationDto> passengerList;
    private String UserFullName;

}
