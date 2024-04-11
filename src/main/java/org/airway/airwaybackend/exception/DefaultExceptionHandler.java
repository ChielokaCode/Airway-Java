package org.airway.airwaybackend.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;
@RestControllerAdvice
public class DefaultExceptionHandler {
    @ExceptionHandler(UserNotVerifiedException.class)
    public ResponseEntity<ApiError> handleUserNotVerifiedException(
            UserNotVerifiedException e, HttpServletRequest request) {
        return buildErrorResponse(request, HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(UserNotEligibleException.class)
    public ResponseEntity<ApiError> handleUserNotEligibleException(
            UserNotEligibleException e, HttpServletRequest request) {
        return buildErrorResponse(request, HttpStatus.BAD_REQUEST, e.getMessage());
    }

    @ExceptionHandler(AirlineNotFoundException.class)
    public ResponseEntity<ApiError> AirlineNotFoundException(
            UserNotVerifiedException e, HttpServletRequest request) {
        return buildErrorResponse(request, HttpStatus.NOT_FOUND, e.getMessage());
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiError> UserNotFoundException(
            UserNotVerifiedException e, HttpServletRequest request) {
        return buildErrorResponse(request, HttpStatus.NOT_FOUND, e.getMessage());
    }
    @ExceptionHandler(SeatListNotFoundException.class)
    public ResponseEntity<ApiError> SeatListNotFoundException(
            UserNotVerifiedException e, HttpServletRequest request) {
        return buildErrorResponse(request, HttpStatus.NOT_FOUND, e.getMessage());
    }


    @ExceptionHandler(PassengerNotFoundException.class)
    public ResponseEntity<ApiError> PassengerNotFoundException(
            UserNotVerifiedException e, HttpServletRequest request) {
        return buildErrorResponse(request, HttpStatus.NOT_FOUND, e.getMessage());
    }



    private ResponseEntity<ApiError> buildErrorResponse(
            HttpServletRequest request, HttpStatus status, String message) {
        return buildErrorResponse(request, status, message, null);

    }

    @ExceptionHandler(ClassNotFoundException.class)
    public ResponseEntity<String> handleClassNotFound(
            ClassNotFoundException e, HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());

    }

    private ResponseEntity<ApiError> buildErrorResponse(
            HttpServletRequest request, HttpStatus status, String message, List<ValidationError> errors) {
        ApiError apiError = new ApiError(
                request.getRequestURI(),
                message,
                status.value(),
                LocalDateTime.now(),
                errors
        );
        return new ResponseEntity<>(apiError, status);
    }
}
