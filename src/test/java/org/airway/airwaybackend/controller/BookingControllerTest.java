package org.airway.airwaybackend.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.airway.airwaybackend.dto.BookingRequestDto;
import org.airway.airwaybackend.serviceImpl.BookingServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingControllerTest {
    @Mock
    private BookingServiceImpl bookingServiceImp;
    @Mock
    HttpServletRequest request;

    @InjectMocks
    private BookingController bookingController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBookFlight() {
        BookingRequestDto bookingRequestDto = new BookingRequestDto();
        String responseMessage = "Booking successful";

        when(bookingServiceImp.bookFlight(bookingRequestDto, request)).thenReturn(responseMessage);

        ResponseEntity<String> responseEntity = bookingController.BookFlight(bookingRequestDto, request);


        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(responseMessage, responseEntity.getBody());

        verify(bookingServiceImp, times(1)).bookFlight(bookingRequestDto, request);
    }
}