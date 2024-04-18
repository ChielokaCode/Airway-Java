package org.airway.airwaybackend.controller;


import jakarta.servlet.http.HttpServletRequest;
import org.airway.airwaybackend.dto.*;
import org.airway.airwaybackend.event.RegistrationCompleteEvent;
import org.airway.airwaybackend.model.User;
import org.airway.airwaybackend.serviceImpl.EmailServiceImpl;
import org.airway.airwaybackend.serviceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@CrossOrigin(origins = {"http://localhost:5173", "https://airway-ng.netlify.app"}, allowCredentials = "true")
@RequestMapping("/api/v1/auth")
public class AuthController {
    private ApplicationEventPublisher publisher;
    private EmailServiceImpl emailService;
    private final UserServiceImpl userService;
    @Autowired
    public AuthController(ApplicationEventPublisher publisher, EmailServiceImpl emailService, UserServiceImpl userService) {
        this.publisher = publisher;
        this.emailService = emailService;
        this.userService = userService;
    }

    @PostMapping("/passenger-sign-up")
    public ResponseEntity<String> signUpUser(@RequestBody SignupDto signupDto, final HttpServletRequest request){
        User user = userService.saveUser(signupDto);
        publisher.publishEvent(new RegistrationCompleteEvent(user, emailService.applicationUrl(request)));
        return new ResponseEntity<>("Signup successful, go to your mail to verify your account", HttpStatus.OK);
    }

    @GetMapping("/verifyRegistration")
    public ResponseEntity<String> verifyRegistration(@RequestParam("token") String token){
        String result = userService.validateVerificationToken(token);
        if (result.equalsIgnoreCase("valid")){
            return new ResponseEntity<>( "User Verified Successfully",HttpStatus.OK);
        }
        return new ResponseEntity<>("User not Verified", HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto){
String result = userService.logInUser(loginDto);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @PostMapping("/changePassword")
    public ResponseEntity <String> changePassword(@RequestBody ChangePasswordDto passwordDto) {
        return new ResponseEntity<>(userService.changeUserPassword(passwordDto), HttpStatus.OK);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody EmailSenderDto passwordDto, HttpServletRequest request){
        userService.forgotPassword(passwordDto, request);
        return new ResponseEntity<>("Forgot password email successfully sent", HttpStatus.OK);

    }

    @PostMapping("/reset-password/{token}")
    public ResponseEntity<String> resetPassword(@PathVariable String token, @RequestBody ResetPasswordDto passwordDto) {
        return userService.resetPassword(token, passwordDto);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request){
        String result = userService.logoutUser(request);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
