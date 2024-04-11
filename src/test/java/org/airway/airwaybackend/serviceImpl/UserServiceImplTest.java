package org.airway.airwaybackend.serviceImpl;

import org.airway.airwaybackend.dto.LoginDto;
import org.airway.airwaybackend.dto.ResetPasswordDto;
import org.airway.airwaybackend.exception.PasswordsDontMatchException;
import org.airway.airwaybackend.exception.UserNotVerifiedException;
import org.airway.airwaybackend.model.PasswordResetToken;
import org.airway.airwaybackend.model.User;
import org.airway.airwaybackend.repository.PasswordResetTokenRepository;
import org.airway.airwaybackend.repository.UserRepository;
import org.airway.airwaybackend.repository.VerificationTokenRepository;
import org.airway.airwaybackend.utils.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Calendar;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;
    @Mock
    private EmailServiceImpl emailService;

    @MockBean
    private JwtUtils jwtUtils;

    @InjectMocks
    private UserServiceImpl userService;

    AutoCloseable autoCloseable ;

    @BeforeEach
    public void setup() {
        autoCloseable=  MockitoAnnotations.openMocks(this);
    }
    @Test
    void testLoginUser_UsrNotVerified() {
        User mockUser = new User();
        mockUser.setEmail("test@gmail.com");
        mockUser.setIsEnabled(false);
        mockUser.setPassword(passwordEncoder.encode("1234"));


        jwtUtils = mock(JwtUtils.class);


        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("test@gmail.com");
        loginDto.setPassword("1234");
        assertFalse(mockUser.getIsEnabled());

    }

    @Test
    void userFound_ReturnsUser() {

        String userEmail = "test@example.com";
        User mockUser = new User();
        mockUser.setEmail(userEmail);


        UserRepository userRepositoryMock = mock(UserRepository.class);
        PasswordEncoder passwordEncoderMock = mock(PasswordEncoder.class);
        JwtUtils jwtUtilsMock = mock(JwtUtils.class);
        EmailServiceImpl emailServiceMock = mock(EmailServiceImpl.class);
        PasswordResetTokenRepository passwordResetTokenRepositoryMock = mock(PasswordResetTokenRepository.class);

      when(userRepositoryMock.save(mockUser)).thenReturn(mockUser);
        UserDetails userDetails = userRepositoryMock.save(mockUser);
       assertEquals(userEmail, mockUser.getEmail());

    }

    @Test
    void loadUserByUsername_UserNotFound_ThrowsUsernameNotFoundException() {
        String userEmail = "nonexistent@example.com";

        UserRepository userRepositoryMock = mock(UserRepository.class);
        PasswordEncoder passwordEncoderMock = mock(PasswordEncoder.class);
        JwtUtils jwtUtilsMock = mock(JwtUtils.class);
        EmailServiceImpl emailServiceMock = mock(EmailServiceImpl.class);
        PasswordResetTokenRepository passwordResetTokenRepositoryMock = mock(PasswordResetTokenRepository.class);


        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(userEmail));

    }

    
}
