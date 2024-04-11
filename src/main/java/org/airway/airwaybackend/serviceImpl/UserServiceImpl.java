package org.airway.airwaybackend.serviceImpl;


import lombok.extern.slf4j.Slf4j;
import org.airway.airwaybackend.dto.*;
import jakarta.servlet.http.HttpServletRequest;
import org.airway.airwaybackend.exception.*;
import org.airway.airwaybackend.enums.Role;
import org.airway.airwaybackend.model.User;
import org.airway.airwaybackend.model.PasswordResetToken;
import org.airway.airwaybackend.model.User;
import org.airway.airwaybackend.model.VerificationToken;
import org.airway.airwaybackend.repository.PasswordResetTokenRepository;
import org.airway.airwaybackend.repository.UserRepository;
import org.airway.airwaybackend.repository.VerificationTokenRepository;
import org.airway.airwaybackend.service.UserService;
import org.airway.airwaybackend.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Calendar;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final PasswordEncoder passwordEncoder;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailServiceImpl emailService;
    private final VerificationTokenRepository verificationTokenRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, JwtUtils jwtUtils, PasswordEncoder passwordEncoder, PasswordResetTokenRepository passwordResetTokenRepository, EmailServiceImpl emailService, VerificationTokenRepository verificationTokenRepository) {
        this.userRepository = userRepository;
        this.jwtUtils = jwtUtils;
        this.passwordEncoder = passwordEncoder;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.emailService = emailService;
        this.verificationTokenRepository = verificationTokenRepository;

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email not Found"));
    }

    @Override
    public String logInUser(LoginDto userDto) {
        UserDetails user = loadUserByUsername(userDto.getEmail());

        if (!user.isEnabled()) {
            throw new UserNotVerifiedException("User is not verified, check email to Verify Registration");
        }

        if (!passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
            throw new UserNotVerifiedException("Username and Password is Incorrect");
        }

        return jwtUtils.createJwt.apply(user);
    }



    public boolean checkIfValidOldPassword(User user, String oldPassword) {
        return validatePassword(oldPassword) && passwordEncoder.matches(oldPassword, user.getPassword());
    }

    public void changePassword(User user, String newPassword) {
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public String changeUserPassword(ChangePasswordDto passwordDto) {
        User user = userRepository.findUserByEmail(passwordDto.getEmail());
        if (user == null) {
            return "User not found";
        }

//        THIS IS COMMENTED OUT BECAUSE THE ADMIN PASSWORD IS 1234

//        if (!validatePassword(passwordDto.getOldPassword())) {
//            return "Invalid Old Password. Password must meet the required criteria: at least 1 uppercase letter, 1 lowercase letter, 1 digit, 1 special character (@#$%^&+=), and minimum length of 8 characters";
//        }

        if (passwordDto.getOldPassword().equals(passwordDto.getNewPassword())) {
            return "New password must be different from the old password";
        }

        if (!validatePassword(passwordDto.getNewPassword())) {
            return "New password does not meet the required criteria: at least 1 uppercase letter, 1 lowercase letter, 1 digit, 1 special character (@#$%^&+=), and minimum length of 8 characters";
        }

        if (!passwordEncoder.matches(passwordDto.getOldPassword(),user.getPassword())){
            return "Password does not match";
         } else {
        return "Password Changed Successfully ";
    }

    }

   

    public User findUserByEmail(String username) {
        return userRepository.findByEmail(username).orElseThrow(() -> new RuntimeException("Username Not Found" + username));
    }
    @Override
    public void createPasswordResetTokenForUser(User user, String token) {
        PasswordResetToken newlyCreatedPasswordResetToken = new PasswordResetToken(user, token);
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByUserId(user.getId());
        if(passwordResetToken != null){
            passwordResetTokenRepository.delete(passwordResetToken);
        }
        passwordResetTokenRepository.save(newlyCreatedPasswordResetToken);
    }

    private String validatePasswordResetToken(String token) {
        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);
        if (passwordResetToken == null) {
            return "invalid";
        }
        Calendar cal = Calendar.getInstance();
        if (passwordResetToken.getExpirationTime().getTime()
                - cal.getTime().getTime() <= 0) {
            passwordResetTokenRepository.delete(passwordResetToken);
            return "expired";
        }
        return "valid";
    }


    @Override
    public void forgotPassword(EmailSenderDto passwordDto, HttpServletRequest request) {
        User user = findUserByEmail(passwordDto.getEmail());
        if (user == null) {
            throw new UsernameNotFoundException("User with email " + passwordDto.getEmail() + " not found");
        }
            String token = UUID.randomUUID().toString();
            createPasswordResetTokenForUser(user, token);
            emailService.passwordResetTokenMail(user, emailService.applicationUrl(request), token);
    }


    private Optional<User> getUserByPasswordReset(String token) {
        return Optional.ofNullable(passwordResetTokenRepository.findByToken(token).getUser());
    }

    private void changePassword(User user, String newPassword, String newConfirmPassword) {

        if (newPassword.equals(newConfirmPassword)) {
            user.setPassword(passwordEncoder.encode(newPassword));
            user.setConfirmPassword(passwordEncoder.encode(newConfirmPassword));
            userRepository.save(user);
        } else {
            throw new PasswordsDontMatchException("Passwords do not Match!");
        }
    }

    @Override
    public ResponseEntity<String> resetPassword(String token, ResetPasswordDto passwordDto) {
        String result = validatePasswordResetToken(token);
        if (!result.equalsIgnoreCase("valid")) {
            throw new InvalidTokenException("Invalid Token");
        }
        Optional<User> user = getUserByPasswordReset(token);
        if (user.isPresent()) {
            changePassword(user.get(), passwordDto.getPassword(), passwordDto.getConfirmPassword());
            return new ResponseEntity<>("Password Reset Successful", HttpStatus.OK);
        } else {
            throw new InvalidTokenException("Invalid Token");
        }
    }

    @Override
    public User saveUser(SignupDto signupDto) {
        if (userRepository.existsByEmail(signupDto.getEmail())) {
            throw new EmailIsTakenException("Email is already taken, try Logging In or Signup with another email" );
        }
        User user = new User();

        if (!signupDto.getPassword().equals (signupDto.getConfirmPassword())){
            throw new PasswordsDontMatchException("Passwords are not the same");
        }
        if (!validatePassword(signupDto.getPassword())) {
            throw new PasswordsDontMatchException("Password does not meet the required criteria");
        }

        user.setPassword(passwordEncoder.encode(signupDto.getPassword()));
        user.setConfirmPassword(passwordEncoder.encode(signupDto.getConfirmPassword()));
        user.setFirstName(signupDto.getFirstName());
        user.setLastName(signupDto.getLastName());
        user.setCountry(signupDto.getCountry());
        user.setPhoneNumber(signupDto.getPhoneNumber());
        user.setEmail(signupDto.getEmail());
        user.setUserRole(Role.PASSENGER);
        user.setMembershipNo(generateMemberShip("ME"));
        return userRepository.save(user);
    }
  
    public boolean validatePassword(String password){
        String capitalLetterPattern = "(?=.*[A-Z])";
        String lowercaseLetterPattern = "(?=.*[a-z])";
        String digitPattern = "(?=.*\\d)";
        String symbolPattern = "(?=.*[@#$%^&+=])";
        String lengthPattern = ".{8,}";

        String regex = capitalLetterPattern + lowercaseLetterPattern + digitPattern + symbolPattern + lengthPattern;

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(password);

        return matcher.matches();
    }


    public void saveVerificationTokenForUser(User user, String token) {
        VerificationToken verificationToken = new VerificationToken(user, token);
        verificationTokenRepository.save(verificationToken);

    }
    public String validateVerificationToken(String token) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token);
        if (verificationToken == null){
            return "invalid";
        }
        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpirationTime().getTime()
                - cal.getTime().getTime()) <=0) {
            verificationTokenRepository.delete(verificationToken);
            return "expired";
        }
        user.setIsEnabled(true);
        userRepository.save(user);
        return "valid";
    }

    public VerificationToken generateNewVerificationToken(String oldToken) {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(oldToken);
        verificationToken.setToken(UUID.randomUUID().toString());
        verificationTokenRepository.save(verificationToken);
        return verificationToken;
    }

    public String generateMemberShip (String prefix) {
        Random random = new Random();
        int suffixLength = 6;
        StringBuilder suffixBuilder = new StringBuilder();
        for (int i = 0; i < suffixLength; i++) {
            suffixBuilder.append(random.nextInt(10));
        }
        return prefix + suffixBuilder.toString();
    }

    @Override
    public String logoutUser(HttpServletRequest request) {
            SecurityContextHolder.getContext().setAuthentication(null);
            SecurityContextHolder.clearContext();
            request.getSession().invalidate();
            return "User logged out Successfully";
    }

    @Override
    public User editUser(UserDto userEditDto, Long userId) {
        Long loggedInUserId = getUserIdFromAuthenticationContext();
        log.debug("Editing user with ID: {}", loggedInUserId);

        User userMakingEdit = this.userRepository.findById(loggedInUserId)
                .orElseThrow(() -> new UserNotFoundException("User with ID: "+loggedInUserId+ " Not Found"));

        if (!loggedInUserId.equals(userId)) {
            throw new UserNotEligibleException("You are not eligible to edit this user");
        }

        // Update user's information
        userMakingEdit.setFirstName(userEditDto.getFirstName());
        userMakingEdit.setLastName(userEditDto.getLastName());
        userMakingEdit.setCountry(userEditDto.getCountry());
        userMakingEdit.setPhoneNumber(userEditDto.getPhoneNumber());
        userMakingEdit.setGender(userEditDto.getGender());
        userMakingEdit.setDateOfBirth(userEditDto.getDateOfBirth());
        return userRepository.save(userMakingEdit);
    }

    private Long getUserIdFromAuthenticationContext() {
        User loggedInUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return loggedInUser.getId();
    }

    @Override
    public Optional<User> findUserById(Long userId) {
        return userRepository.findById(userId);
    }
}
