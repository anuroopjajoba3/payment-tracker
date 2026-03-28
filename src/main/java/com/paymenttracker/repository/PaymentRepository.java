package com.paymenttracker.repository;

import com.paymenttracker.model.Payment;
import com.paymenttracker.model.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    List<Payment> findBySenderId(String senderId);

    List<Payment> findByReceiverId(String receiverId);

    List<Payment> findByStatus(PaymentStatus status);

    List<Payment> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT p FROM Payment p WHERE p.senderId = :senderId AND p.status = :status")
    List<Payment> findBySenderIdAndStatus(String senderId, PaymentStatus status);

    @Query("SELECT SUM(p.amount) FROM Payment p WHERE p.senderId = :senderId AND p.status = 'COMPLETED'")
    BigDecimal getTotalSentBySender(String senderId);
}