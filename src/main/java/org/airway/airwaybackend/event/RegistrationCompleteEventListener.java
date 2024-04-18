package org.airway.airwaybackend.event;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.airway.airwaybackend.model.User;
import org.airway.airwaybackend.serviceImpl.EmailServiceImpl;
import org.airway.airwaybackend.serviceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {
    private final UserServiceImpl userService;

    private final EmailServiceImpl emailService;

    private final JavaMailSender javaMailSender;
    @Autowired
    public RegistrationCompleteEventListener(UserServiceImpl userService,
                                             EmailServiceImpl emailService, JavaMailSender javaMailSender) {
        this.userService = userService;
        this.emailService = emailService;
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void onApplicationEvent(RegistrationCompleteEvent event) {

        User user = event.getUser();
        String token = UUID.randomUUID().toString();
        userService.saveVerificationTokenForUser(user, token);

        String url = event.getApplicationUrl() + "/verifyRegistration?token=" + token;

        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(user.getEmail());
            helper.setSubject("Verification Email");

            String imageHtml = "<div><img style=\"width: 100%; display: inline-block;\" src=\"cid:welcomeVerify\"></div><br><br>";

            String welcomeUser = "<h1 style=\"text-align: center;\">Welcome " + user.getFirstName() + "!</h1><br>";

            String text = "<h2>You're almost ready to get started. <br>Please Click on the button below to verify your account:</h2> <br><br>" +
                    "<div style=\"text-align: center;\"><a href=\"" + url + "\" style=\"background-color: #001F3F; color: #ffffff; text-decoration: none; padding: 10px 20px; border-radius: 10px; cursor: pointer; display: inline-block; width: 250px; height: 35px; font-size: 20px;\">Verify Account</a></div><br><br><br>" +
                    "<h2>Best regards,<br>Airway</h2>";

            String emailBody = "<div style=\"width: 50%; margin: 0 auto;\">" + imageHtml + welcomeUser + text + "</div>";


            helper.setText(emailBody, true);

            ClassPathResource resource = new ClassPathResource("airwayWelcomeBanner.png");
            helper.addInline("welcomeVerify", resource);

            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
