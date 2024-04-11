package org.airway.airwaybackend.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.airway.airwaybackend.enums.BookingStatus;
import org.airway.airwaybackend.enums.FlightDirection;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingEditingDto {
    private FlightDirection tripType;
    private List<PassengerDTo> passengers;
    private List<BookingFlightDto> bookingFlights;

    @Enumerated(EnumType.STRING)
    private BookingStatus bookingStatus;
}
