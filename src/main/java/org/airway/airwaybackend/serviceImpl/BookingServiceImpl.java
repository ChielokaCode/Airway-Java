package org.airway.airwaybackend.serviceImpl;


import jakarta.servlet.http.HttpServletRequest;
import org.airway.airwaybackend.dto.*;
import org.airway.airwaybackend.enums.BookingStatus;
import org.airway.airwaybackend.enums.FlightDirection;
import org.airway.airwaybackend.enums.FlightStatus;
import org.airway.airwaybackend.enums.Role;
import org.airway.airwaybackend.exception.*;
import org.airway.airwaybackend.exception.ClassNotFoundException;
import org.airway.airwaybackend.model.*;
import org.airway.airwaybackend.repository.*;
import org.airway.airwaybackend.model.Booking;
import org.airway.airwaybackend.repository.BookingRepository;
import org.airway.airwaybackend.service.BookingService;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;

import static java.lang.Boolean.FALSE;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final PassengerRepository passengerRepository;
    private final UserRepository userRepository;
    private final FlightRepository flightRepository;
    private final ClassesRepository classesRepository;
    private final SeatListRepository seatListRepository;
    private final BookingFlightRepository bookingFlightRepository;
    private final PNRServiceImpl pnrServiceImpl;
    private final EmailServiceImpl emailService;
    private final BookingConfirmationTokenRepository bookingConfirmationTokenRepository;
    private final Map<String, String> bookingReferenceMap = new HashMap<>();

    public BookingServiceImpl(BookingRepository bookingRepository, PassengerRepository passengerRepository, UserRepository userRepository, FlightRepository flightRepository, ClassesRepository classesRepository, SeatListRepository seatListRepository, BookingFlightRepository bookingFlightRepository, PNRServiceImpl pnrServiceImpl, EmailServiceImpl emailService, BookingConfirmationTokenRepository bookingConfirmationTokenRepository) {
        this.bookingRepository = bookingRepository;
        this.passengerRepository = passengerRepository;
        this.userRepository = userRepository;
        this.flightRepository = flightRepository;
        this.classesRepository = classesRepository;
        this.seatListRepository = seatListRepository;
        this.bookingFlightRepository = bookingFlightRepository;
        this.pnrServiceImpl = pnrServiceImpl;
        this.emailService = emailService;
        this.bookingConfirmationTokenRepository = bookingConfirmationTokenRepository;
    }

    @Override
    public Page<Booking> getAllBookings(int pageNo, int pageSize, String sortParam) {
        try {
            String [] sortParams= sortParam.split(",");
            Sort sort = Sort.by(sortParams[0]);
            if(sortParams.length ==2){
                sort = sortParams[1].equalsIgnoreCase("asc")? sort.ascending():sort.descending();
            }
            Pageable pageable = PageRequest.of(pageNo, pageSize, sort);
            return bookingRepository.findAll(pageable);
        }catch (Exception e){
            e.printStackTrace();
            throw new BookingNotFoundException("An error occurred");
        }
    }

    @Override
    public int getTotalNumberOfBookings() {
        return (int) bookingRepository.count();
    }



    @Override
    public String bookFlight(BookingRequestDto bookingRequestDto, HttpServletRequest request) {
        try {
            Booking booking = new Booking();
            List<PNR> pnrList = new ArrayList<>();
            PNR pnr;
            booking.setBookingStatus(BookingStatus.PENDING);
            Booking savedBooking = bookingRepository.save(booking);

            List<PassengerDTo> passengerDTos = bookingRequestDto.getPassengers();
            List<Passenger> passengers = new ArrayList<>();
            for (int i = 0; i < passengerDTos.size(); i++) {
                PassengerDTo passengerDTo = passengerDTos.get(i);
                Passenger passenger = new Passenger();
                passenger.setFirstName(passengerDTo.getFirstName());
                passenger.setPassengerEmail(passengerDTo.getPassengerEmail());
                passenger.setCategory(passengerDTo.getCategory());
                passenger.setContact(passengerDTo.getContact());
                passenger.setGender(passengerDTo.getGender());
                passenger.setContactPhone(passengerDTo.getContactPhone());
                passenger.setPhoneNumber(passengerDTo.getPhoneNumber());
                passenger.setContactEmail(passengerDTo.getContactEmail());
                passenger.setDateOfBirth(passengerDTo.getDateOfBirth());
                passenger.setNationality(passengerDTo.getNationality());
                passenger.setTitle(passengerDTo.getTitle());
                passenger.setLastName(passengerDTo.getLastName());
                passenger.setBookings(savedBooking);
                passenger.setPSN(String.format("PSN%03d", i + 1));
                passenger.setContactEmail(passengerDTo.getContactEmail());
                passenger.setContactEmail(passengerDTo.getContactEmail());
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                String username = authentication.getName();
                User user = userRepository.findUserByEmail(username);
                if (user == null) {
                    passenger.setPassengerCode(generateMemberShip("GU"));
                    if (passenger.getContact().equals(true)) {
                        booking.setPassengerCode(passenger.getPassengerCode());
                        booking.setPassengerContactEmail(passenger.getPassengerEmail());
                    }
                } else if (user != null && user.getUserRole().equals(Role.PASSENGER)) {
                    booking.setUserId(user);
                    passenger.setPassengerCode(generateMemberShip("GU"));
                    if (passenger.getContact().equals(true)) {
                        booking.setPassengerCode(user.getMembershipNo());
                        booking.setPassengerContactEmail(passenger.getPassengerEmail());
                    }
                } else if (user != null && user.getUserRole().equals(Role.ADMIN)) {
                    booking.setUserId(user);
                    passenger.setPassengerCode(generateMemberShip("AD"));
                    if (passenger.getContact().equals(true)) {
                        booking.setPassengerCode(passenger.getPassengerCode());
                        booking.setPassengerContactEmail(passenger.getPassengerEmail());

                    }
                }
                passengers.add(passenger);
            }
            List<Passenger> savedPassengers = passengerRepository.saveAll(passengers);
            List<BookingFlightDto> bookingFlightDtos = bookingRequestDto.getBookingFlights();
            List<BookingFlight> bookingFlights = new ArrayList<>();
            for (BookingFlightDto bookingFlightDto : bookingFlightDtos) {
                BookingFlight bookingFlight = new BookingFlight();
                Classes classes = classesRepository.findById(bookingFlightDto.getClassId()).orElseThrow(() -> new ClassNotFoundException("classes not found"));
                if (classes.getSeat().getAvailableSeat() == 0) {
                    throw new SeatListNotFoundException("No Seat is available");
                }
                Flight flight = classes.getFlight();

                bookingFlight.setFlight(flight);
                bookingFlight.setBaseFare(calculateFare(classes.getBaseFare(), savedPassengers.size()));
                bookingFlight.setBaggageAllowance(String.valueOf(calculateBaggageAllowance(classes.getBaggageAllowance(), savedPassengers.size())));
                bookingFlight.setServiceCharge(calculateFare(classes.getServiceCharge(), savedPassengers.size()));
                bookingFlight.setSurchargeFee(calculateFare(classes.getSurchargeFee(), savedPassengers.size()));
                bookingFlight.setTaxFee(calculateFare(classes.getTaxFee(), savedPassengers.size()));
                bookingFlight.setTotalFare(calculateFare(classes.getTotalFare(), passengers.size()));
                bookingFlight.setClasses(classes);
                bookingFlight.setBooking(savedBooking);
                bookingFlights.add(bookingFlight);
            }
            List<BookingFlight> savedBookingFlight = bookingFlightRepository.saveAll(bookingFlights);
            for (BookingFlight bookingFlight : savedBookingFlight) {
                List<PNR> classPnrs = bookingFlight.getClasses().getPnrList();
                pnr = pnrServiceImpl.generatePNRForEachPassengerAndFlight(bookingFlight, savedPassengers);
               pnr.setPassengerList(savedPassengers);
               pnr.setBookingFlight(bookingFlight);
                pnrList.add(pnr);
                bookingFlight.setPnr(pnr);
                classPnrs.add(pnr);
                classesRepository.save(bookingFlight.getClasses());
                bookingFlightRepository.save(bookingFlight);
            }

            booking.setBookingFlights(savedBookingFlight);
            booking.setPassengers(savedPassengers);
            booking.setBookingFlights(savedBookingFlight);
            Set<String> usedNumbers = new HashSet<>();
            booking.setBookingReferenceCode(generateBookingReferenceNumber(usedNumbers));
            booking.setPnrList(pnrList);
            booking.setPay(FALSE);
            if(bookingFlights.size()==2){
            booking.setTripType(FlightDirection.ROUND_TRIP);
            } else if (bookingFlights.size()==1) {
                booking.setTripType(FlightDirection.ONE_WAY);
            }
            booking.setTotalFare(getALLtotalFare(savedBookingFlight));
            booking.setSurchargeFee(calculateTotal(savedBookingFlight, BookingFlight::getSurchargeFee));
            booking.setTaxFee(calculateTotal(savedBookingFlight, BookingFlight::getTaxFee));
            booking.setBaseFare(calculateTotal(savedBookingFlight, BookingFlight::getBaseFare));
            booking.setServiceCharge(calculateTotal(savedBookingFlight, BookingFlight::getServiceCharge));
            booking.setBaggageAllowance(calculateAllBaggageAllowances(savedBookingFlight, BookingFlight::getBaggageAllowance));
            booking.setPassengers(savedPassengers);

            savedBooking = bookingRepository.save(booking);
            for(Passenger passenger : savedPassengers){
                passenger.setBookingReference(savedBooking.getBookingReferenceCode());
            }
            bookingConfirmationMails(request, savedBooking.getBookingReferenceCode());
            String token = generateTokenForBookingReference(savedBooking.getBookingReferenceCode());
            return "Booking Successful:"+token;
        } catch (ClassNotFoundException ex) {
            return "Class not available";
        } catch (SeatListNotFoundException ex) {
            return "Seat not Available";
        } catch (FlightNotFoundException ex) {
            return "Flight not Available";
        } catch (Exception e) {
            e.printStackTrace();
            return "Error occurred in the process";
        }
    }

    @Override
    public BigDecimal calculateFare(BigDecimal fare, int passenger) {
        return fare.multiply(BigDecimal.valueOf(passenger));
    }

    @Override
    public BigDecimal getALLtotalFare(List<BookingFlight> flights) {
        BigDecimal totalFare = BigDecimal.ZERO;
        for (BookingFlight bookingFlight : flights) {
            if (bookingFlight != null) {
                totalFare = totalFare.add(bookingFlight.getTotalFare());
            }
        }
        return totalFare;
    }

    @Override
    public String generateBookingReferenceNumber(Set<String> usedNumber) {
        String prefix = "XY";
        int digit = 6;
        Random random = new Random();

        String randomNumber;
        do {
            randomNumber = String.format("%06d", random.nextInt(1000000));

        } while (usedNumber.contains(prefix + randomNumber));
        String bookingReferenceNumber = prefix + randomNumber;
        usedNumber.add(bookingReferenceNumber);
        return bookingReferenceNumber;
    }

    public String generateMemberShip(String prefix) {
        Random random = new Random();
        int suffixLength = 6;
        StringBuilder suffixBuilder = new StringBuilder();
        for (int i = 0; i < suffixLength; i++) {
            suffixBuilder.append(random.nextInt(10));
        }
        return prefix + suffixBuilder.toString();
    }

    @Override
    public String calculateBaggageAllowance(String weightString, double factor) {
        String weightValue = weightString.replaceAll("[^\\d.]+", "");
        double weightInNumeric = Double.parseDouble(weightValue);
        double calculatedWeight = weightInNumeric * factor;
        return String.format("%.2f kg", calculatedWeight);
    }

    @Override
    public String calculateAllBaggageAllowances(List<BookingFlight> flights, Function<BookingFlight, String> propertyExtractor) {
        double totalWeight = 0.0;
        for (BookingFlight flight : flights) {
            if (flight != null) {
                String baggageAllowance = propertyExtractor.apply(flight);
                String weightValue = baggageAllowance.replaceAll("[^\\d.]+", "");
                double weightInNumeric = Double.parseDouble(weightValue);
                totalWeight += weightInNumeric;
            }
        }
        return String.format("%.2f kg", totalWeight);
    }

    @Override
    public BigDecimal calculateTotal(List<BookingFlight> flights, Function<BookingFlight, BigDecimal> propertyExtractor) {
        BigDecimal total = BigDecimal.ZERO;
        for (BookingFlight flight : flights) {
            if (flight != null) {
                total = total.add(propertyExtractor.apply(flight));
            }
        }
        return total;
    }


    public String editBookingById(Long id, BookingEditingDto bookingEditingDto) throws IllegalArgumentException, ClassNotFoundException {
        if (bookingEditingDto == null || bookingEditingDto.getPassengers() == null) {
            throw new IllegalArgumentException("Booking sata is missing");
        }
        Booking booking = getBookingById(id);
        updateBookingDetail(booking, bookingEditingDto);
        List<Passenger> savedPassengersList = updatePassengerDetails(booking, bookingEditingDto.getPassengers());
        List<BookingFlight> savedBookingFlights = updateBookingFlights(booking, bookingEditingDto.getBookingFlights());
        booking.setPassengers(savedPassengersList);
        booking.setBookingFlights(savedBookingFlights);
        return "Booking updated Successfully";
    }
    public Booking getBookingById(Long id){
        return bookingRepository.findById(id)
                .orElseThrow(()-> new BookingNotFoundException("Booking not found with id:" + id));
    }

    public void updateBookingDetail (Booking booking, BookingEditingDto bookingEditingDto){
        if(bookingEditingDto.getTripType()!= null) {
            booking.setTripType(bookingEditingDto.getTripType());
        }
        if(bookingEditingDto.getBookingStatus()!=null) {
            booking.setBookingStatus(bookingEditingDto.getBookingStatus());
        }
        bookingRepository.save(booking);
    }
    private List<Passenger> updatePassengerDetails(Booking booking, List<PassengerDTo> passengerDtos) {
        List<Passenger> passengersList = booking.getPassengers();
        List<Passenger> savedPassengersList = new ArrayList<>();
        for (int i = 0; i < Math.min(passengerDtos.size(), passengersList.size()); i++) {
            PassengerDTo passengerDto = passengerDtos.get(i);
            Passenger passenger = passengersList.get(i);
            passenger.setFirstName(passengerDto.getFirstName());
            passenger.setPassengerEmail(passengerDto.getPassengerEmail());
            passenger.setContactEmail(passengerDto.getContactEmail());
            passenger.setContact(passengerDto.getContact());
            passenger.setDateOfBirth(passengerDto.getDateOfBirth());
            passenger.setPhoneNumber(passengerDto.getPhoneNumber());
            passenger.setCategory(passengerDto.getCategory());
            passenger.setGender(passengerDto.getGender());
            passenger.setNationality(passengerDto.getNationality());
            passenger.setTitle(passengerDto.getTitle());
            passenger.setContactPhone(passengerDto.getContactPhone());
            passenger.setLastName(passengerDto.getLastName());
            passenger.setPSN(String.format("PSN%03d", i + 1));
            passenger.setPassengerCode(passengerDto.getPassengerCode());
            if(passenger.getContact()){
                booking.setPassengerContactEmail(passenger.getPassengerEmail());
            }
            passenger.setBookings(booking);
            passenger = passengerRepository.save(passenger);
            savedPassengersList.add(passenger);
        }
        return savedPassengersList;
    }
    private List<BookingFlight> updateBookingFlights(Booking booking, List<BookingFlightDto> bookingFlightDtos) throws ClassNotFoundException {
        List<BookingFlight> bookingFlights = booking.getBookingFlights();
        List<BookingFlight> savedBookingFlights = new ArrayList<>();

        for (int i = 0; i < Math.min(bookingFlightDtos.size(), bookingFlights.size()); i++) {
            BookingFlightDto bookingFlightDto = bookingFlightDtos.get(i);
            BookingFlight bookingFlight = bookingFlights.get(i);
            Classes classes = classesRepository.findById(bookingFlightDto.getClassId())
                    .orElseThrow(() -> new ClassNotFoundException("Class not Available"));
            if (classes.getSeat().getAvailableSeat() == 0) {
                throw new SeatListNotFoundException("No Seat is available");
            }
            bookingFlight.setClasses(classes);
            Flight flight = classes.getFlight();
            bookingFlight.setFlight(flight);
            List<Passenger> passengers = booking.getPassengers();

            bookingFlight.setBaseFare(calculateFare(classes.getBaseFare(), passengers.size()));
            bookingFlight.setBaggageAllowance(String.valueOf(calculateBaggageAllowance(classes.getBaggageAllowance(), passengers.size())));
            bookingFlight.setServiceCharge(calculateFare(classes.getServiceCharge(), passengers.size()));
            bookingFlight.setSurchargeFee(calculateFare(classes.getSurchargeFee(), passengers.size()));
            bookingFlight.setTaxFee(calculateFare(classes.getTaxFee(), passengers.size()));
            bookingFlight.setTotalFare(calculateFare(classes.getTotalFare(), passengers.size()));
            bookingFlight.setClasses(classes);
            bookingFlight.setBooking(booking);
            bookingFlightRepository.save(bookingFlight);
            savedBookingFlights.add(bookingFlight);

        }
        return savedBookingFlights;

    }

    public void bookingConfirmationMails(HttpServletRequest request, String bookingReferenceCode) {
        Booking booking = bookingRepository.findByBookingReferenceCode(bookingReferenceCode).orElseThrow(() -> new RuntimeException("Booking not registered"));
        if (booking == null) {
            throw new UsernameNotFoundException("Booking with referenceCode " + bookingReferenceCode + " not found");
        }
        String token = UUID.randomUUID().toString();
        createBookingConfirmationTokenForBooking(booking, token);
        emailService.sendBookingConfirmationMail(booking, token, request);
    }


    public void TicketConfirmationMails(HttpServletRequest request, String bookingReferenceCode) {
        Booking booking = bookingRepository.findByBookingReferenceCode(bookingReferenceCode).orElseThrow(() -> new RuntimeException("Booking not registered"));
        if (booking == null) {
            throw new BookingNotFoundException("Booking with referenceCode " + bookingReferenceCode + " not found");
        }
        String token = UUID.randomUUID().toString();
        createBookingConfirmationTokenForBooking(booking, token);
        emailService.sendTicketingConfirmationMail(booking, token, request);
    }

    public void createBookingConfirmationTokenForBooking(Booking booking, String token) {
        BookingConfirmationToken newlyCreatedBookingConfirmationToken = new BookingConfirmationToken(booking, token);
        BookingConfirmationToken bookingConfirmationToken = bookingConfirmationTokenRepository.findByBooking(booking);
        if (bookingConfirmationToken != null) {
            bookingConfirmationTokenRepository.delete(bookingConfirmationToken);
        }
        bookingConfirmationTokenRepository.save(newlyCreatedBookingConfirmationToken);
    }

    public String validateBookingConfirmationToken(String token) {
        BookingConfirmationToken bookingConfirmationToken = bookingConfirmationTokenRepository.findByToken(token);
        if (bookingConfirmationToken == null) {
            return "invalid";
        }
        Calendar cal = Calendar.getInstance();
        if ((bookingConfirmationToken.getExpirationTime().getTime()
                - cal.getTime().getTime()) <= 0) {
            bookingConfirmationTokenRepository.delete(bookingConfirmationToken);
            return "expired";
        } else return "valid";
    }


    private Optional<Booking> getBookingByToken(String token) {
        BookingConfirmationToken bookingConfirmationToken = bookingConfirmationTokenRepository.findByToken(token);
        if (bookingConfirmationToken != null) {
            return Optional.ofNullable(bookingConfirmationToken.getBooking());
        }
        return Optional.empty();
    }


    public BookingConfirmationDto confirmBooking(String token) {
        String result = validateBookingConfirmationToken(token);
        if (!result.equalsIgnoreCase("valid")) {
            throw new InvalidTokenException("Invalid Token");
        }
        Optional<Booking> booking = getBookingByToken(token);
        BookingConfirmationDto bookingConfirmationDto = null;
        if (booking.isPresent()) {
            bookingConfirmationDto = new BookingConfirmationDto();
            List<PassengerConfirmationDto> passengerConfirmationDtos = new ArrayList<>();
            List<PnrDto> pnrDtoList = new ArrayList<>();
            List<FlightConfirmDTo> flightConfirmDTos = new ArrayList<>();
            bookingConfirmationDto.setPassengerList(passengerConfirmationDtos);
            List<BookingFlight> bookingFlights = booking.get().getBookingFlights();
            for (BookingFlight bookingFlight : bookingFlights) {
                FlightConfirmDTo flightConfirmDTo = new FlightConfirmDTo();
                flightConfirmDTo.setFlightNo(bookingFlight.getFlight().getFlightNo());
                flightConfirmDTo.setArrivalPortIata(bookingFlight.getFlight().getArrivalPort().getIataCode());
                flightConfirmDTo.setDeparturePortIata(bookingFlight.getFlight().getDeparturePort().getIataCode());
                flightConfirmDTo.setArrivalDate(String.valueOf(bookingFlight.getFlight().getArrivalDate()));
                flightConfirmDTo.setDepartureDate(String.valueOf(bookingFlight.getFlight().getDepartureDate()));
                flightConfirmDTo.setArrivalTime(String.valueOf(bookingFlight.getFlight().getArrivalTime()));
                flightConfirmDTo.setDepartureTime(String.valueOf(bookingFlight.getFlight().getDepartureTime()));
                flightConfirmDTo.setArrivalPortCity(bookingFlight.getFlight().getArrivalPort().getCity());
                flightConfirmDTo.setDeparturePortCity(bookingFlight.getFlight().getDeparturePort().getCity());
                flightConfirmDTo.setPassengerList(passengerConfirmationDtos);
                flightConfirmDTos.add(flightConfirmDTo);
            }
            bookingConfirmationDto.setFlightDetails(flightConfirmDTos);

            List<Passenger> passenger = passengerRepository.findAllByPassengerEmail(booking.get().getPassengerContactEmail());
            bookingConfirmationDto.setUserFullName(passenger.get(passenger.size() - 1).getFirstName() + "  " + passenger.get(passenger.size() - 1).getLastName());

            List<PNR> pnrs = booking.get().getPnrList();
            for (PNR pnr : pnrs) {
                PnrDto pnrDto = new PnrDto();
                pnrDto.setPNRCode(pnr.getPNRCode());
                pnrDtoList.add(pnrDto);
            }

            bookingConfirmationDto.setPNRCodes(pnrDtoList);
            bookingConfirmationDto.setBookingRef(booking.get().getBookingReferenceCode());
            List<Passenger> passengerList = booking.get().getPassengers();
            for (Passenger passenger2 : passengerList) {
                PassengerConfirmationDto passengerConfirmationDto = new PassengerConfirmationDto();
                passengerConfirmationDto.setFirstName(passenger2.getFirstName());
                passengerConfirmationDto.setLastName(passenger2.getLastName());
                passengerConfirmationDto.setTitle(passenger2.getTitle());
                passengerConfirmationDtos.add(passengerConfirmationDto);
            }
            bookingConfirmationDto.setExpiryTime(String.valueOf(booking.get().getCreatedAt().toLocalTime().plusHours(24)));
            bookingConfirmationDto.setExpiryDate(String.valueOf(booking.get().getCreatedAt().toLocalDate().plusDays(1)));
            bookingConfirmationDto.setPassengerList(passengerConfirmationDtos);
            return bookingConfirmationDto;
        }
        throw new InvalidTokenException("Invalid Token");

    }

    public TicketConfirmationDto confirmTicket (String token) {
        Optional<Booking> bookingOptional = getBookingByToken(token);
        if (bookingOptional.isPresent()) {
            Booking booking = bookingOptional.get();
            TicketConfirmationDto ticketConfirmationDto = new TicketConfirmationDto();
            ticketConfirmationDto.setBookingRef(booking.getBookingReferenceCode());
            List<FlightConfirmDTo> flightConfirmDTos = new ArrayList<>();
            List<BookingFlight> bookingFlightList = booking.getBookingFlights();

            for (BookingFlight bookingFlight : bookingFlightList) {
                PNR pnr = bookingFlight.getPnr();
                List<Passenger> passengers = pnr.getPassengerList();

                List<PassengerConfirmationDto> passengerConfirmationDtos = new ArrayList<>();
                for (Passenger passenger : passengers) {
                    PassengerConfirmationDto passengerConfirmationDto = new PassengerConfirmationDto();
                    List<Ticket> tickets = passenger.getTickets();
                    if (!tickets.isEmpty()) {
                        passengerConfirmationDto.setTicketNo(tickets.get(bookingFlightList.indexOf(bookingFlight)).getTicketNo());
                    }

                    List<SeatList> seats = passenger.getSeat();
                    if (!seats.isEmpty()) {
                        passengerConfirmationDto.setSeatNo(seats.get(bookingFlightList.indexOf(bookingFlight)).getSeatLabel());
                    }
                    passengerConfirmationDto.setTitle(passenger.getTitle());
                    passengerConfirmationDto.setBaggageAllowance(bookingFlight.getBaggageAllowance());
                    passengerConfirmationDto.setFirstName(passenger.getFirstName());
                    passengerConfirmationDto.setLastName(passenger.getLastName());
                    passengerConfirmationDtos.add(passengerConfirmationDto);
                }

                FlightConfirmDTo flightConfirmDTo = new FlightConfirmDTo();
                flightConfirmDTo.setFlightNo(bookingFlight.getFlight().getFlightNo());
                flightConfirmDTo.setArrivalPortIata(bookingFlight.getFlight().getArrivalPort().getIataCode());
                flightConfirmDTo.setDeparturePortIata(bookingFlight.getFlight().getDeparturePort().getIataCode());
                flightConfirmDTo.setArrivalDate(String.valueOf(bookingFlight.getFlight().getArrivalDate()));
                flightConfirmDTo.setDepartureDate(String.valueOf(bookingFlight.getFlight().getDepartureDate()));
                flightConfirmDTo.setArrivalTime(String.valueOf(bookingFlight.getFlight().getArrivalTime()));
                flightConfirmDTo.setDepartureTime(String.valueOf(bookingFlight.getFlight().getDepartureTime()));
                flightConfirmDTo.setArrivalPortCity(bookingFlight.getFlight().getArrivalPort().getCity());
                flightConfirmDTo.setDeparturePortCity(bookingFlight.getFlight().getDeparturePort().getCity());
                flightConfirmDTo.setBagageAllowance(bookingFlight.getBaggageAllowance());
                flightConfirmDTo.setPassengerList(passengerConfirmationDtos);

                flightConfirmDTos.add(flightConfirmDTo);
            }

            ticketConfirmationDto.setFlightDetails(flightConfirmDTos);
            List<Passenger> passenger = passengerRepository.findAllByPassengerEmail(booking.getPassengerContactEmail());
            ticketConfirmationDto.setUserFullName(passenger.get(passenger.size() - 1).getFirstName() + "  " + passenger.get(passenger.size() - 1).getLastName());
            ticketConfirmationDto.setBookingRef(booking.getBookingReferenceCode());
            List<PNR> pnrs = booking.getPnrList();
            List<PnrDto> pnrDtoList = new ArrayList<>();
            for (PNR pnr : pnrs) {
                PnrDto pnrDto = new PnrDto();
                pnrDto.setPNRCode(pnr.getPNRCode());
                pnrDtoList.add(pnrDto);
            }

            ticketConfirmationDto.setPNRCode(pnrDtoList);


            return ticketConfirmationDto;
        } else {
            throw new InvalidTokenException("Invalid Token");
        }
    }


    public TripSummaryDTo getTripSummary(String token) throws BookingNotFoundException {
        String bookingRef= returnBookingRef(token);
        if(bookingRef == null || token== null){
            throw new BookingNotFoundException("Token is invalid");
        }
        Booking booking = bookingRepository.findByBookingReferenceCode(bookingRef).orElseThrow(()-> new BookingNotFoundException("Booking not found"));
        TripSummaryDTo tripSummaryDTo = new TripSummaryDTo();
        List<BookingFlight> flightsBooked=booking.getBookingFlights();
        List<FlightConfirmDTo>flightConfirmDTos= new ArrayList<>();
        for(BookingFlight bookingFlight: flightsBooked){
            FlightConfirmDTo flightConfirmDTo = new FlightConfirmDTo();
            flightConfirmDTo.setArrivalTime(String.valueOf(bookingFlight.getFlight().getArrivalTime()));
            flightConfirmDTo.setDepartureTime(String.valueOf(bookingFlight.getFlight().getDepartureTime()));
            flightConfirmDTo.setArrivalDate(String.valueOf(bookingFlight.getFlight().getArrivalDate()));
            flightConfirmDTo.setDepartureDate(String.valueOf(bookingFlight.getFlight().getDepartureDate()));
            flightConfirmDTo.setFlightNo(bookingFlight.getFlight().getFlightNo());
            flightConfirmDTo.setDeparturePortCity(bookingFlight.getFlight().getDeparturePort().getCity());
            flightConfirmDTo.setArrivalPortCity(bookingFlight.getFlight().getArrivalPort().getCity());
            flightConfirmDTo.setClassName(bookingFlight.getClasses().getClassName());
            flightConfirmDTos.add(flightConfirmDTo);
        }
        tripSummaryDTo.setFlightDetails(flightConfirmDTos);
        tripSummaryDTo.setBookingRef(booking.getBookingReferenceCode());
        tripSummaryDTo.setServiceCharge(String.valueOf(booking.getServiceCharge()));
        tripSummaryDTo.setSurCharge(String.valueOf(booking.getSurchargeFee()));
        tripSummaryDTo.setBaseFare(String.valueOf(booking.getBaseFare()));
        tripSummaryDTo.setTaxAmount(String.valueOf(booking.getTaxFee()));
        tripSummaryDTo.setTotalFare(String.valueOf(booking.getTotalFare()));
        return tripSummaryDTo;
    }

    public String generateTokenForBookingReference(String bookingRef){
        String token = UUID.randomUUID().toString();
        bookingReferenceMap.put(token, bookingRef);
        return token;
    }

    public String returnBookingRef (String token){
        return bookingReferenceMap.get(token);
    }


    public String cancelBooking(Long id) {
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new BookingNotFoundException("Booking not found"));
        if (booking.getBookingStatus()==BookingStatus.CANCELLED){
            throw new BookingNotFoundException("Booking already cancelled");
        }
        List<Passenger> passengerList = booking.getPassengers();
        for (Passenger passenger : passengerList) {
            List<SeatList> seatLists = passenger.getSeat();
            for(int i = 0; i<seatLists.size(); i++) {
                SeatList seatList = seatLists.get(i);
                seatList.getSeat().setAvailableSeat(seatList.getSeat().getAvailableSeat() + 1);
                seatList.getSeat().setNoOfOccupiedSeats(seatList.getSeat().getNoOfOccupiedSeats() - 1);
                seatList.setOccupied(false);
                seatList.setAssignedPerson(null);
                seatListRepository.save(seatList);
            }
            passenger.setSeat(null);
            passenger.setTickets(null);
            passengerRepository.save(passenger);
        }
        booking.setBookingStatus(BookingStatus.CANCELLED);
        bookingRepository.save(booking);
        return "Booking cancelled successfully";
    }
}
