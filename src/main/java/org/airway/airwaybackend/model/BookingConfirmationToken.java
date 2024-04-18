package org.airway.airwaybackend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.Date;

@Data
@Entity
@NoArgsConstructor
public class BookingConfirmationToken {
    private static final int EXPIRATION_TIME = 60 * 24;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String token;

    private Date createdDate;

    private Date expirationTime;

    @OneToOne(fetch = FetchType.EAGER)
    private Booking booking;

    public BookingConfirmationToken(Booking booking, String token){
        super();
        this.token = token;
        this.booking = booking;
        this.expirationTime = calculateExpirationDate(EXPIRATION_TIME);

    }

    public BookingConfirmationToken(String token){
        super();
        this.token = token;
        this.expirationTime = calculateExpirationDate(EXPIRATION_TIME);
    }

    private Date calculateExpirationDate(int expirationTime) {
        Calendar calender = Calendar.getInstance();
        calender.setTimeInMillis(new Date().getTime());
        calender.add(Calendar.MINUTE, expirationTime);
        return new Date(calender.getTime().getTime());
    }

}
