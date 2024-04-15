package org.chielokacode.airwaycc.airwaybackendcc.repository;

import org.chielokacode.airwaycc.airwaybackendcc.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
