package org.airway.airwaybackend.repository;

import org.airway.airwaybackend.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
//    Optional<Payment>findByPaymentReference(String ref);

    Optional<Payment> findByTransactionReference(String reference);
}
