package org.airway.airwaybackend.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.airway.airwaybackend.exception.BookingNotFoundException;
import org.airway.airwaybackend.paystack.paymentverify.PaymentVerificationResponse;
import org.airway.airwaybackend.serviceImpl.PaymentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payment")
@CrossOrigin(origins = {"http://localhost:5173", "https://airway-ng.netlify.app"}, allowCredentials = "true")
public class PaymentController {
    private final PaymentServiceImpl paymentService;
    @Autowired
    public PaymentController(PaymentServiceImpl paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/initializingpayment/{bookingRef}")
    public ResponseEntity<?> initializePayment( @PathVariable  String bookingRef) throws ResourceNotFoundException {
    try {
        return paymentService.initializePayment(bookingRef);

    }catch (BookingNotFoundException e){
        return handleException(e, "Error during payment initialization");
    }
    }


    @GetMapping("/verify-payment/{reference}")
    public ResponseEntity<?> verifyPayment(@PathVariable String reference, HttpServletRequest request){
        try{
             PaymentVerificationResponse paymentVerificationResponse=paymentService.verifyTransaction(reference, request);
             return ResponseEntity.ok(paymentVerificationResponse);
        }catch (ResourceNotFoundException e){
            return handleResourceNotFoundException(e, "payment not found");
        }
    }
    private ResponseEntity<?> handleResourceNotFoundException(ResourceNotFoundException e, String errorMessage) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(errorMessage + ": " + e.getMessage());
    }

    private ResponseEntity<?> handleException(Exception e, String errorMessage) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(errorMessage + ": " + e.getMessage());
    }
}
