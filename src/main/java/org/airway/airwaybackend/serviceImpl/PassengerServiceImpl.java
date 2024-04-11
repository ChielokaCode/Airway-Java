package org.airway.airwaybackend.serviceImpl;


import org.airway.airwaybackend.dto.PassengerDTo;
import org.airway.airwaybackend.exception.PassengerNotFoundException;
import org.airway.airwaybackend.model.Booking;
import org.airway.airwaybackend.model.Passenger;
import org.airway.airwaybackend.model.User;
import org.airway.airwaybackend.repository.BookingRepository;
import org.airway.airwaybackend.repository.PassengerRepository;
import org.airway.airwaybackend.repository.UserRepository;
import org.airway.airwaybackend.service.PassengerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class PassengerServiceImpl implements PassengerService {
    private  final PassengerRepository passengerRepository;
    private final BookingRepository bookingRepository;
    private  final UserRepository userRepository;
@Autowired
    public PassengerServiceImpl(PassengerRepository passengerRepository, BookingRepository bookingRepository, UserRepository userRepository) {
        this.passengerRepository = passengerRepository;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
    }

@Override
    public Set<PassengerDTo> findAll() {
        List<Booking> bookings = bookingRepository.findAll();
        Set<PassengerDTo> passengerData = new HashSet<>();
//        Set<String> processedEmails = new HashSet<>();

        for (Booking booking : bookings) {
            String passengerEmail = booking.getPassengerContactEmail();

//            if (processedEmails.contains(passengerEmail)) {
//                continue;
//            }

            PassengerDTo passengerDto = new PassengerDTo();
            passengerDto.setPassengerCode(booking.getPassengerCode());

            if (booking.getUserId() == null) {

                passengerDto.setMembership("GUEST");
                passengerDto.setPassengerEmail(passengerEmail);
                List<Passenger> passengers = passengerRepository.findPassengerByBookingsAndPassengerEmail(booking, passengerEmail).orElseThrow(()->new PassengerNotFoundException("Passenger not found"));
                passengerDto.setPhoneNumber(passengers.get(0).getPhoneNumber());
                passengerDto.setCreated(booking.getCreatedAt().toLocalDate());
                passengerDto.setFirstName(passengers.get(0).getFirstName());
                passengerDto.setLastName(passengers.get(0).getLastName());
                passengerDto.setPassengerCode(booking.getPassengerCode());
            } else {

                User user = userRepository.findById(booking.getUserId().getId())
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));

                passengerDto.setMembership("MEMBER");
                passengerDto.setPassengerEmail(user.getEmail());
                passengerDto.setPhoneNumber(user.getPhoneNumber());
                passengerDto.setCreated(booking.getCreatedAt().toLocalDate());
                passengerDto.setFirstName(user.getFirstName());
                passengerDto.setLastName(user.getLastName());
                passengerDto.setPassengerCode(booking.getPassengerCode());
            }

            passengerData.add(passengerDto);
//            processedEmails.add(passengerEmail);
        }
        return passengerData;
    }
}
