package org.airway.airwaybackend.service;

import jakarta.servlet.http.HttpServletRequest;
import org.airway.airwaybackend.dto.*;
import org.airway.airwaybackend.exception.ClassNotFoundException;
import org.airway.airwaybackend.exception.UnauthorizedAccessException;
import org.airway.airwaybackend.model.BookingFlight;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import org.airway.airwaybackend.model.Booking;
import org.springframework.data.domain.Page;

import org.airway.airwaybackend.dto.BookingRequestDto;
import org.airway.airwaybackend.model.BookingFlight;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public interface BookingService {
    int getTotalNumberOfBookings();
     String cancelBooking(Long id);
    BigDecimal calculateFare(BigDecimal fare, int passenger);
    BigDecimal getALLtotalFare (List<BookingFlight> flights);
    String generateBookingReferenceNumber (Set<String> usedNumber);
    String generateMemberShip (String prefix);
    String calculateBaggageAllowance(String weightString, double factor);
    String calculateAllBaggageAllowances(List<BookingFlight> flights, Function<BookingFlight, String> propertyExtractor);
    BigDecimal calculateTotal(List<BookingFlight> flights, Function<BookingFlight, BigDecimal> propertyExtractor);
     Page<Booking> getAllBookings(int pageNo, int pageSize, String sortParam);

    String editBookingById(Long id, BookingEditingDto bookingEditingDto) throws UnauthorizedAccessException, ClassNotFoundException;
    BookingConfirmationDto confirmBooking(String token);
    TicketConfirmationDto confirmTicket(String token);
    String bookFlight(BookingRequestDto bookingRequestDto, HttpServletRequest request);

    TripSummaryDTo getTripSummary(String token);

}
