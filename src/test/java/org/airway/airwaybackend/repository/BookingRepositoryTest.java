package org.airway.airwaybackend.repository;

import org.airway.airwaybackend.enums.BookingStatus;
import org.airway.airwaybackend.model.Booking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class BookingRepositoryTest {


    @Mock
    private BookingRepository bookingRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByBookingStatusNotAndCreatedAtBefore() {
        BookingStatus bookingStatus = BookingStatus.PENDING;
        LocalDateTime createdAt = LocalDateTime.now();
        Booking booking = new Booking();

        when(bookingRepository.findByBookingStatusNotAndCreatedAtBefore(any(), any())).thenReturn(List.of(booking));

        List <Booking> result = bookingRepository.findByBookingStatusNotAndCreatedAtBefore(bookingStatus, createdAt);

        assertNotNull(result);
    }
}