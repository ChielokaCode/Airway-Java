package org.airway.airwaybackend.service;

import org.airway.airwaybackend.dto.*;
import org.airway.airwaybackend.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.airway.airwaybackend.dto.LoginDto;
import org.airway.airwaybackend.model.User;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface UserService {
    String logInUser(LoginDto userDto);
    void createPasswordResetTokenForUser(User user, String token);
    void forgotPassword(EmailSenderDto passwordDto, HttpServletRequest request);
    ResponseEntity<String> resetPassword(String token, ResetPasswordDto passwordDto);
    User saveUser(SignupDto signupDto);
    String changeUserPassword(ChangePasswordDto passwordDto);
    void saveVerificationTokenForUser(User user, String token);
    String logoutUser(HttpServletRequest request);
    User editUser(UserDto userEditDto, Long userId);
    Optional<User> findUserById(Long userId);


}
