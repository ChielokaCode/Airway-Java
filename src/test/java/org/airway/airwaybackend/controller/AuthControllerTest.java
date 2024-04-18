package org.airway.airwaybackend.controller;

import org.airway.airwaybackend.dto.LoginDto;
import org.airway.airwaybackend.serviceImpl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLogin() {

        LoginDto loginDto = new LoginDto();
        loginDto.setEmail("testUser");
        loginDto.setPassword("testPassword");

        String expectedResult = "Login successful";

        when(userService.logInUser(loginDto)).thenReturn(expectedResult);

        ResponseEntity<String> response = authController.login(loginDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedResult, response.getBody());
        verify(userService, times(1)).logInUser(loginDto);
    }
}