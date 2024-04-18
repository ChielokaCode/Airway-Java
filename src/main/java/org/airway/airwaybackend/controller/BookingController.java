package org.airway.airwaybackend.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.airway.airwaybackend.dto.*;
import org.airway.airwaybackend.exception.BookingNotFoundException;
import org.airway.airwaybackend.exception.ClassNotFoundException;
import org.airway.airwaybackend.exception.UnauthorizedAccessException;
import org.airway.airwaybackend.model.Booking;
import org.airway.airwaybackend.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/booking")
@CrossOrigin(origins = {"http://localhost:5173", "https://airway-ng.netlify.app"}, allowCredentials = "true")
public class BookingController {

        private BookingService bookingService;
        @Autowired
        public BookingController(BookingService bookingService) {
            this.bookingService = bookingService;
        }
    @GetMapping("/bookings")
    public ResponseEntity<Page<Booking>> getAllBookings(
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(name = "sort", required = false ,defaultValue = "createdAt,dsc") String sortParam) {

        return new ResponseEntity<>(bookingService.getAllBookings(pageNo,pageSize,sortParam), HttpStatus.OK);
    }
    @PutMapping("/edit-bookings/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> editBooking(@PathVariable Long id, @RequestBody BookingEditingDto bookingEditingDto) throws UnauthorizedAccessException, ClassNotFoundException {
        bookingService.editBookingById(id, bookingEditingDto);
        return ResponseEntity.ok("Booking updated Successfully");
    }

    @PostMapping("/booking-flight")
    public ResponseEntity<String> BookFlight(@RequestBody BookingRequestDto bookingRequestDto, final HttpServletRequest request) {
        String response = bookingService.bookFlight(bookingRequestDto, request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/booking-confirmation/{token}")
    public ResponseEntity<BookingConfirmationDto> bookingConfirmed(@PathVariable String token){
        return ResponseEntity.ok(bookingService.confirmBooking(token));
    }
    @GetMapping("/ticket-confirmation/{token}")
    public ResponseEntity<TicketConfirmationDto> ticketConfirmed(@PathVariable String token){
        return ResponseEntity.ok(bookingService.confirmTicket(token));
    }

    @GetMapping("/trip-summary/{token}")
    public ResponseEntity<TripSummaryDTo> getTripSummarryDetails (@PathVariable String token) throws BookingNotFoundException {
        try {
            return ResponseEntity.ok(bookingService.getTripSummary(token));
        } catch (BookingNotFoundException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @PutMapping("/booking-cancelling/{id}")
    public ResponseEntity<String> cancelBooking(@PathVariable Long id) {
        String response = bookingService.cancelBooking(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}