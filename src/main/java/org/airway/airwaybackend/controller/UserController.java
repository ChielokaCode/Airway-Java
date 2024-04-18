package org.airway.airwaybackend.controller;

import org.airway.airwaybackend.dto.UserDto;
import org.airway.airwaybackend.model.User;
import org.airway.airwaybackend.serviceImpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = {"http://localhost:5173", "https://airway-ng.netlify.app"}, allowCredentials = "true")
@RequestMapping("/api/v1/user")
public class UserController {
    private UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PutMapping("/edit-user/{userId}")
    @PreAuthorize("hasAnyRole()")
    public ResponseEntity<User> editUserById(@RequestBody UserDto userEditDto,
                                             @PathVariable Long userId){
        User user = userService.editUser(userEditDto, userId);
        return  new ResponseEntity<>(user, HttpStatus.OK);
    }
    @GetMapping("/get-user/{userId}")
    @PreAuthorize("hasAnyRole()")
    public ResponseEntity<Object> getUserById(@PathVariable Long userId) {
        Optional<User> userOptional = userService.findUserById(userId);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            UserDto userDTO = convertToDto(user);
            return ResponseEntity.ok(userDTO);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    private UserDto convertToDto(User user) {
        return new UserDto(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getCountry(),
                user.getPhoneNumber(),
                user.getGender(),
                user.getDateOfBirth()
        );
    }
}
