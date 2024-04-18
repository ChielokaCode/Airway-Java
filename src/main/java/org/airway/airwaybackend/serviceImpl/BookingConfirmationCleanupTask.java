package org.airway.airwaybackend.serviceImpl;
import org.airway.airwaybackend.enums.BookingStatus;
import org.airway.airwaybackend.model.Booking;
import org.airway.airwaybackend.repository.BookingFlightRepository;
import org.airway.airwaybackend.repository.BookingRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class BookingConfirmationCleanupTask {
private final BookingRepository bookingRepository;
private final BookingFlightRepository bookingFlightRepository;
    public BookingConfirmationCleanupTask(BookingRepository bookingRepository, BookingFlightRepository bookingFlightRepository) {
        this.bookingRepository = bookingRepository;
        this.bookingFlightRepository = bookingFlightRepository;
    }

    @Scheduled(fixedDelay = 24 * 60 * 60 * 1000)
    public void deleteExpiredUserConfirmations() {
        LocalDateTime twentyFourHoursAgo = LocalDateTime.now().minusHours(24);
        List<Booking> unconfirmedExpiredBookings = bookingRepository.findByBookingStatusNotAndCreatedAtBefore(BookingStatus.CONFIRMED, twentyFourHoursAgo);
for(Booking booking : unconfirmedExpiredBookings){
    booking.setBookingStatus(BookingStatus.CANCELLED);
}
        bookingRepository.saveAll(unconfirmedExpiredBookings);
    }
}
