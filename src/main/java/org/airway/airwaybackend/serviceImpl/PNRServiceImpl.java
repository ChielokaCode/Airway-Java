package org.airway.airwaybackend.serviceImpl;

import org.airway.airwaybackend.model.BookingFlight;
import org.airway.airwaybackend.model.PNR;
import org.airway.airwaybackend.model.Passenger;
import org.airway.airwaybackend.repository.PNRRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

@Service
public class PNRServiceImpl {
private final PNRRepository pnrRepository;

    public PNRServiceImpl(PNRRepository pnrRepository) {
        this.pnrRepository = pnrRepository;
    }
public PNR generatePNRForEachPassengerAndFlight(BookingFlight bookingFlight , List<Passenger> passengerList){
    Set<String> usedNumbers = new HashSet<>();
        PNR pnr = new PNR();
        pnr.setBookingFlight(bookingFlight);
        pnr.setPassengerList(passengerList);
        pnr.setPNRCode(generateBookingPNR(usedNumbers));
    return pnrRepository.save(pnr);
}
    public String generateBookingPNR (Set<String> usedNumber){
        String prefix = "PYV";
        int digit = 4;
        Random random = new Random();

        String randomNumber;
        do{
            randomNumber = String.format("%06d", random.nextInt(10000));

        }while (usedNumber.contains(prefix+randomNumber));
        String bookingReferenceNumber= prefix+ randomNumber;
        usedNumber.add(bookingReferenceNumber);
        return bookingReferenceNumber;
    }

}
