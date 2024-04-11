package org.airway.airwaybackend.repository;

import org.airway.airwaybackend.model.Booking;
import org.airway.airwaybackend.model.BookingConfirmationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingConfirmationTokenRepository extends JpaRepository<BookingConfirmationToken, Long> {
    BookingConfirmationToken findByToken(String token);

    BookingConfirmationToken findByBooking(Booking booking);
}
