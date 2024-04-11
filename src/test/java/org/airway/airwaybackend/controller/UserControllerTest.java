package org.airway.airwaybackend.controller;

import org.airway.airwaybackend.dto.UserDto;
import org.airway.airwaybackend.model.User;
import org.airway.airwaybackend.serviceImpl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private UserController userController;

    @Test
    public void testEditUserById() {
        UserDto mockUserDto = new UserDto("John", "Doe", "john@example.com", "USA", "123456789", "Male", "1990-01-01");
        User mockUser = new User();
        when(userService.editUser(any(UserDto.class), anyLong())).thenReturn(mockUser);

        ResponseEntity<User> response = userController.editUserById(mockUserDto, 1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockUser, response.getBody());
    }


    @Test
    public void testGetUserById_UserFound() {
        // Mocking user data
        User mockUser = new User();
        mockUser.setFirstName("John");
        mockUser.setLastName("Doe");
        mockUser.setEmail("john@example.com");
        mockUser.setCountry("USA");
        mockUser.setPhoneNumber("123456789");
        mockUser.setGender("Male");
        mockUser.setDateOfBirth("1990-01-01");
        Optional<User> userOptional = Optional.of(mockUser);
        when(userService.findUserById(anyLong())).thenReturn(userOptional);

        ResponseEntity<Object> response = userController.getUserById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        UserDto userDto = (UserDto) response.getBody();
        assert userDto != null;
        assertEquals("John", userDto.getFirstName());
        assertEquals("Doe", userDto.getLastName());
        assertEquals("john@example.com", userDto.getEmail());
        assertEquals("USA", userDto.getCountry());
        assertEquals("123456789", userDto.getPhoneNumber());
        assertEquals("Male", userDto.getGender());
        assertEquals("1990-01-01", userDto.getDateOfBirth());
    }

    @Test
    public void testGetUserById_UserNotFound() {
        Optional<User> userOptional = Optional.empty();
        when(userService.findUserById(anyLong())).thenReturn(userOptional);

        ResponseEntity<Object> response = userController.getUserById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
}