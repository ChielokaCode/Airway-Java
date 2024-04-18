package org.airway.airwaybackend.serviceImpl;

import org.airway.airwaybackend.model.BookingFlight;
import org.airway.airwaybackend.model.Passenger;
import org.airway.airwaybackend.model.Ticket;
import org.airway.airwaybackend.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Service
public class TicketServiceImpl {
    private final TicketRepository ticketRepository;
    public TicketServiceImpl(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }
    public List<Ticket> generateTicketForEachPassengerAndFlight(BookingFlight bookingFlight , Passenger passenger){
        List<Ticket> passengerTickets = passenger.getTickets();
        Set<String> usedNumbers = new HashSet<>();
        Ticket ticket = new Ticket();
        ticket.setPassenger(passenger);
        ticket.setBookingFlight(bookingFlight);
        ticket.setBooking(bookingFlight.getBooking());
        ticket.setTicketNo(generateBookingTicket(usedNumbers,bookingFlight.getFlight().getDeparturePort().getIataCode()+bookingFlight.getFlight().getArrivalPort().getIataCode()));
        Ticket savedTicket= ticketRepository.save(ticket);
         passengerTickets.add(savedTicket);
         return passengerTickets;
    }
    public String generateBookingTicket (Set<String> usedNumber, String prefix ){
        Random random = new Random();

        String randomNumber;
        do{
            randomNumber = String.format("%06d", random.nextInt(10000));

        }while (usedNumber.contains(prefix+randomNumber));
        String ticketNo= prefix+ randomNumber;
        usedNumber.add(ticketNo);
        return ticketNo;
    }

}
