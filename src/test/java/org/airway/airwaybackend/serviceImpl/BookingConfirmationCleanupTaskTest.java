package org.airway.airwaybackend.serviceImpl;

import org.airway.airwaybackend.enums.BookingStatus;
import org.airway.airwaybackend.model.Booking;
import org.airway.airwaybackend.repository.BookingFlightRepository;
import org.airway.airwaybackend.repository.BookingRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookingConfirmationCleanupTaskTest {
    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private BookingFlightRepository bookingFlightRepository;

    @InjectMocks
    private BookingConfirmationCleanupTask bookingConfirmationCleanupTask;

    public BookingConfirmationCleanupTaskTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testDeleteExpiredUserConfirmations() {
        // Given
        LocalDateTime twentyFourHoursAgo = LocalDateTime.now().minusHours(24);
        List<Booking> unconfirmedExpiredBookings = new ArrayList<>();
        Booking booking1 = new Booking();
        booking1.setBookingStatus(BookingStatus.PENDING);
        booking1.setCreatedAt(LocalDateTime.now().minusHours(25));
        unconfirmedExpiredBookings.add(booking1);

        when(bookingRepository.findByBookingStatusNotAndCreatedAtBefore(eq(BookingStatus.CONFIRMED), any(LocalDateTime.class)))
                .thenReturn(unconfirmedExpiredBookings);

        bookingConfirmationCleanupTask.deleteExpiredUserConfirmations();

        verify(bookingRepository, times(1)).findByBookingStatusNotAndCreatedAtBefore(eq(BookingStatus.CONFIRMED), any(LocalDateTime.class));
        verify(bookingRepository, times(1)).saveAll(unconfirmedExpiredBookings);

        assertEquals(BookingStatus.CANCELLED, booking1.getBookingStatus());
    }
}