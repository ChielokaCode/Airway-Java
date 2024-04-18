package org.airway.airwaybackend.serviceImpl;


import com.google.api.client.util.Value;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.airway.airwaybackend.enums.BookingStatus;
import org.airway.airwaybackend.enums.Category;
import org.airway.airwaybackend.exception.BookingNotFoundException;
import org.airway.airwaybackend.exception.SeatListNotFoundException;
import org.airway.airwaybackend.model.*;
import org.airway.airwaybackend.paystack.paymentinit.PaymentInitializationResponse;
import org.airway.airwaybackend.paystack.paymentverify.PaymentVerificationResponse;
import org.airway.airwaybackend.repository.*;
import org.airway.airwaybackend.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    @Value("${applyforme.paystack.secret.key}")
    private String paystackSecretKey;

    private final WebClient webClient = WebClient.builder()
            .baseUrl("https://api.paystack.co")
            .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer sk_test_002fcf762bffb8f02deedfdea1e09c94fe3db328")
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .filter(ExchangeFilterFunction.ofRequestProcessor(clientRequest -> {
//                clientRequest.headers().add(HttpHeaders.AUTHORIZATION, "Bearer sk_test_002fcf762bffb8f02deedfdea1e09c94fe3db328");
                log.info("Request Headers: {}", clientRequest.headers());
                return Mono.just(clientRequest);
            }))
            .build();
    private final UserRepository userRepository;
    private final PaymentRepository paymentRepository;
    private final EmailServiceImpl emailService;
    private final BookingRepository bookingRepository;
    private final PassengerRepository passengerRepository;
    private final SeatListRepository seatListRepository;
    private final TicketServiceImpl ticketServiceImpl;
    private final BookingFlightRepository bookingFlightRepository;
    private final BookingServiceImpl bookingServiceImpl;

    @Autowired
    public PaymentServiceImpl(UserRepository userRepository, PaymentRepository paymentRepository,
                              EmailServiceImpl emailService, BookingRepository bookingRepository, PassengerRepository passengerRepository, SeatListRepository seatListRepository, TicketServiceImpl ticketServiceImpl, BookingFlightRepository bookingFlightRepository, BookingServiceImpl bookingServiceImpl) {
        this.userRepository = userRepository;
        this.paymentRepository = paymentRepository;
        this.emailService = emailService;
        this.bookingRepository = bookingRepository;
        this.passengerRepository = passengerRepository;
        this.seatListRepository = seatListRepository;
        this.ticketServiceImpl = ticketServiceImpl;
        this.bookingFlightRepository = bookingFlightRepository;
        this.bookingServiceImpl = bookingServiceImpl;
    }

    public ResponseEntity<?> initializePayment(String bookingRef) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        User user = userRepository.findUserByEmail(username);
        Booking booking = bookingRepository.findByBookingReferenceCode(bookingRef).orElseThrow(() -> new BookingNotFoundException("Booking Not found"));
        if(booking.getBookingStatus()==BookingStatus.CANCELLED){
            throw new BookingNotFoundException("Booking reservation is status cancelled");
        }
        Payment payment = paymentRepository.findByTransactionReference(bookingRef).orElse(null);
        if(Objects.nonNull(payment)){
            return new ResponseEntity<>("User has already make payment  " , HttpStatus.INTERNAL_SERVER_ERROR);
        }
        double amount = 0;
        amount = booking.getTotalFare().doubleValue()*100;
        //amount = calculateTotalLoanAmount(initializePaymentDto.getAmount());
        String reference = UUID.randomUUID().toString();
        String CALL_BACK_URL = "https://39c9-197-210-227-220.ngrok-free.app/confirmation-page/" + reference;
        Payment paymentInfo = Payment.builder()
                .email(booking.getPassengerContactEmail())
                .booking(booking)
                .paymentDate(LocalDateTime.now())
                .amount(amount)
                .currency("NGN")
                .paymentReference(reference)
                .callBackUrl(CALL_BACK_URL)
                .user(user)
                .build();
        try {
            PaymentInitializationResponse initiateResponse = webClient
                    .post()
                    .uri("/transaction/initialize")
                    .bodyValue(paymentInfo)
                    .retrieve()
                    .bodyToMono(PaymentInitializationResponse.class)
                    .block();

            if (initiateResponse != null) {
                String transactionReference = initiateResponse.getData().getReference();
                paymentInfo.setTransactionReference(transactionReference);
                paymentInfo.setAmount(amount/100);
                paymentRepository.save(paymentInfo);
                return new ResponseEntity<>(initiateResponse, HttpStatus.OK);
            }
            return new ResponseEntity<>("Error initiating payment", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (WebClientResponseException e) {
            return new ResponseEntity<>("Error initiating payment:  " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    public synchronized PaymentVerificationResponse verifyTransaction(String reference, HttpServletRequest request) {
        try {
            Payment paymentInfo = paymentRepository.findByTransactionReference(reference)
                    .orElseThrow(() -> new ResourceNotFoundException("Payment not found"));
            Booking booking = paymentInfo.getBooking();
            if (booking.getBookingStatus() == BookingStatus.CONFIRMED) {
                throw new BookingNotFoundException("Booking already confirmed");
            }

            PaymentVerificationResponse verificationResponse = webClient
                    .get()
                    .uri("/transaction/verify/" + reference)
                    .header(HttpHeaders.AUTHORIZATION, "Bearer sk_test_002fcf762bffb8f02deedfdea1e09c94fe3db328")
                    .retrieve()
                    .bodyToMono(PaymentVerificationResponse.class)
                    .block();

            if (verificationResponse != null && "success".equals(verificationResponse.getData().getStatus())) {
                paymentInfo.setIsVerified(true);
                paymentRepository.save(paymentInfo);

                booking = bookingRepository.findByBookingReferenceCode(booking.getBookingReferenceCode())
                        .orElseThrow(() -> new RuntimeException("Booking not found"));

                List<BookingFlight> bookingFlights = booking.getBookingFlights();
                List<Passenger> passengers = booking.getPassengers();

                booking.setPay(true);
                booking.setBookingStatus(BookingStatus.CONFIRMED);

                for (BookingFlight bookingFlight : bookingFlights) {
                    List<SeatList> seatLists = seatListRepository
                            .findAllBySeat_IdAndOccupied(bookingFlight.getClasses().getId(), false)
                            .orElseThrow(() -> new SeatListNotFoundException("Seat not available"));
                    for (int i = 0; i < Math.min(passengers.size(), seatLists.size()); i++) {
                        Passenger passenger = passengers.get(i);
                        List<SeatList> passengerAllSeat = passenger.getSeat();
                        SeatList seatList = seatLists.get(i);

                        passenger.setTickets(ticketServiceImpl.generateTicketForEachPassengerAndFlight(bookingFlight, passenger));

                        if (!passenger.getCategory().equals(Category.INFANT)) {
                            if (seatList.getOccupied().equals(true)) {
                                throw new SeatListNotFoundException("Seat not available");
                            }
                            seatList.setOccupied(true);
                            seatList.setAssignedPerson(passenger);
                            seatList.getSeat().setAvailableSeat(seatList.getSeat().getAvailableSeat() - 1);
                            seatList.getSeat().setNoOfOccupiedSeats(seatList.getSeat().getNoOfOccupiedSeats() + 1);
                            passengerAllSeat.add(seatList);
                            seatListRepository.save(seatList);
                        }
                        passengerRepository.save(passenger);
                    }
                    bookingFlightRepository.save(bookingFlight);
                }

                Booking savedBooking = bookingRepository.save(booking);
                bookingServiceImpl.TicketConfirmationMails(request, savedBooking.getBookingReferenceCode());

                return new PaymentVerificationResponse(true, "Verified Successfully", null);
            } else {
                return new PaymentVerificationResponse(false, "Payment verification failed", null);
            }

        } catch (WebClientResponseException e) {
            String errorMessage = "Payment Verification Failed: " + e.getMessage();
            log.error(errorMessage);
            return new PaymentVerificationResponse(false, errorMessage, null);
        }
    }

}
