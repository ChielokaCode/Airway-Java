package org.airway.airwaybackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.airway.airwaybackend.enums.FlightDirection;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingRequestDto {
    private FlightDirection tripType;
    private List<PassengerDTo> passengers;
   private List<BookingFlightDto>bookingFlights;
}
