package org.airway.airwaybackend.serviceImpl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import org.airway.airwaybackend.exception.PassengerNotFoundException;
import org.airway.airwaybackend.model.Booking;
import org.airway.airwaybackend.model.Passenger;
import org.airway.airwaybackend.model.User;
import org.airway.airwaybackend.repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmailServiceImpl  {
    @Value("${spring.mail.username}")
    private String fromEmail;

    @Autowired
    private JavaMailSender javaMailSender;

    private final PassengerRepository passengerRepository;

    @Autowired
    public EmailServiceImpl(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }

    public String sendMail(String to, String subject, String body) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(body);

            javaMailSender.send(mimeMessage);
            return "Mail sent successfully";

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public String applicationUrl(HttpServletRequest request) {
        return "http://" +
                "airway-ng.netlify.app" +
//                request.getServerName() +
//                ":" +
//                "5173" +
                "/api/v1/auth" +
                request.getContextPath();
    }

    public void passwordResetTokenMail(User user, String applicationUrl, String token) {
        String url = applicationUrl + "/reset-password/" + token;

        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(user.getEmail());
            helper.setSubject("Password Reset Email");

            String imageHtml = "<div><img style=\"width: 100%; display: inline-block;\" src=\"cid:reset\"></div><br><br>";

            String welcomeUser = "<h1 style=\"text-align: center;\">Dear " + user.getFirstName() + "!</h1><br>";

            String text = "<h2>You recently requested to reset your password. <br>To proceed with the password reset process, please click on the button below:</h2> <br>" +
                    "<div style=\"text-align: center;\"><a href=\"" + url + "\" style=\"background-color: #001F3F; color: #ffffff; text-decoration: none; padding: 10px 20px; border-radius: 10px; cursor: pointer; display: inline-block; width: 250px; height: 35px; font-size: 20px;\">Reset Password</a></div><br>" +
                    "<h3>If you did not request this change, please ignore this email or contact our support team immediately.</h3>" +
                    "<h3>Thank you.</h3>" +
                    "<h2>Best regards,<br>Airway</h2>";

            String emailBody = "<div style=\"width: 50%; margin: 0 auto;\">" + imageHtml + welcomeUser + text + "</div>";

            helper.setText(emailBody, true);

            ClassPathResource resource = new ClassPathResource("airwayForgotPasswordBanner.png");
            helper.addInline("reset", resource);

            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


    public void sendBookingConfirmationMail(Booking booking, String token, HttpServletRequest request) {
        List<Passenger> passengers = passengerRepository.findPassengerByBookingsAndPassengerEmail(booking, booking.getPassengerContactEmail()).orElseThrow(()-> new PassengerNotFoundException("no passengers found"));
        Passenger passengerContact =passengers.get(0);
        String passengerFirstName = passengerContact.getFirstName();

        String url = "http://" +
                "airway-ng.netlify.app" +
//                request.getServerName() +
//                ":" +
//                "5173" +
                request.getContextPath() + "/booking-confirmation/" + token;

        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(booking.getPassengerContactEmail());
            helper.setSubject("Booking Confirmation Email");

            String imageHtml = "<div><img style=\"width: 100%; display: inline-block;\" src=\"cid:bookingConfirmation\"></div><br><br>";

            String welcomeUser = "<h1 style=\"text-align: center;\">Dear " + passengerFirstName + "!</h1><br>";

            String text = "<h2>We are excited to confirm your booking with us! <br>Please click the button below to confirm your booking: </h2><br><br>" +
                    "<div style=\"text-align: center;\"><a href=\"" + url + "\" style=\"background-color: #001F3F; color: #ffffff; text-decoration: none; padding: 10px 20px; border-radius: 10px; cursor: pointer; display: inline-block; width: 250px; height: 35px; font-size: 20px;\">Confirm Booking</a></div><br><br>" +
                    "<h3>Thank you for choosing us. We look forward to serving you on your upcoming journey.</h3>" +
                    "<h2>Best regards,<br>Airway</h2>";

            String emailBody = "<div style=\"width: 50%; margin: 0 auto;\">" + imageHtml + welcomeUser + text + "</div>";

            helper.setText(emailBody, true);

            ClassPathResource resource = new ClassPathResource("airwayBookingConfirmationBanner.png");
            helper.addInline("bookingConfirmation", resource);

            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendTicketingConfirmationMail(Booking booking, String token, HttpServletRequest request) {
        List<Passenger> passengers = passengerRepository.findPassengerByBookingsAndPassengerEmail(booking, booking.getPassengerContactEmail()).orElseThrow(()-> new PassengerNotFoundException("Passengers not Found"));
        Passenger passengerContact =passengers.get(0);
        String passengerFirstName = passengerContact.getFirstName();

        String url = "http://" +
                "airway-ng.netlify.app" +
//                request.getServerName() +
//                ":" +
//                "5173" +
                request.getContextPath() + "/ticket-confirmation/" + token;

        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(booking.getPassengerContactEmail());
            helper.setSubject("Ticketing Confirmation Email");

            String imageHtml = "<div><img style=\"width: 100%; display: inline-block;\" src=\"cid:ticketConfirmation\"></div><br><br>";

            String welcomeUser = "<h1 style=\"text-align: center;\">Dear " + passengerFirstName + "!</h1><br>";

            String text = "<h2>Your ticket has been successfully booked. <br>Please Click on the button below to confirm your ticket:</h2> <br><br>" +
                    "<div style=\"text-align: center;\"><a href=\"" + url + "\" style=\"background-color: #001F3F; color: #ffffff; text-decoration: none; padding: 10px 20px; border-radius: 10px; cursor: pointer; display: inline-block; width: 250px; height: 35px; font-size: 20px;\">Confirm Ticket</a></div><br><br><br>" +
                    "<h3>Thank you for choosing us. We look forward to serving you on your upcoming journey.</h3>" +
                    "<h2>Best regards,<br>Airway</h2>";

            String emailBody = "<div style=\"width: 50%; margin: 0 auto;\">" + imageHtml + welcomeUser + text + "</div>";

            helper.setText(emailBody, true);

            ClassPathResource resource = new ClassPathResource("airwayTicketConfirmationBanner.png");
            helper.addInline("ticketConfirmation", resource);

            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

}
