package com.paymenttracker.service;

import com.paymenttracker.dto.PaymentDTO;
import com.paymenttracker.exception.PaymentNotFoundException;
import com.paymenttracker.model.Payment;
import com.paymenttracker.model.PaymentStatus;
import com.paymenttracker.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;

    @Transactional
    public PaymentDTO.Response createPayment(PaymentDTO.CreateRequest request) {
        log.info("Creating payment from {} to {} for {} {}",
            request.getSenderId(), request.getReceiverId(),
            request.getAmount(), request.getCurrency());

        Payment payment = Payment.builder()
                .senderId(request.getSenderId())
                .receiverId(request.getReceiverId())
                .amount(request.getAmount())
                .currency(request.getCurrency())
                .description(request.getDescription())
                .status(PaymentStatus.PENDING)
                .build();

        Payment saved = paymentRepository.save(payment);
        return toResponse(saved);
    }

    public PaymentDTO.Response getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found with id: " + id));
        return toResponse(payment);
    }

    public List<PaymentDTO.Response> getAllPayments() {
        return paymentRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<PaymentDTO.Response> getPaymentsBySender(String senderId) {
        return paymentRepository.findBySenderId(senderId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public List<PaymentDTO.Response> getPaymentsByStatus(PaymentStatus status) {
        return paymentRepository.findByStatus(status)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public PaymentDTO.Response updatePaymentStatus(Long id, PaymentDTO.UpdateStatusRequest request) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment not found with id: " + id));

        log.info("Updating payment {} status from {} to {}", id, payment.getStatus(), request.getStatus());
        payment.setStatus(request.getStatus());
        Payment updated = paymentRepository.save(payment);
        return toResponse(updated);
    }

    @Transactional
    public void deletePayment(Long id) {
        if (!paymentRepository.existsById(id)) {
            throw new PaymentNotFoundException("Payment not found with id: " + id);
        }
        paymentRepository.deleteById(id);
    }

    public PaymentDTO.SummaryResponse getSenderSummary(String senderId) {
        List<Payment> payments = paymentRepository.findBySenderId(senderId);
        BigDecimal totalSent = paymentRepository.getTotalSentBySender(senderId);

        PaymentDTO.SummaryResponse summary = new PaymentDTO.SummaryResponse();
        summary.setSenderId(senderId);
        summary.setTotalPayments(payments.size());
        summary.setCompletedPayments(payments.stream()
                .filter(p -> p.getStatus() == PaymentStatus.COMPLETED).count());
        summary.setPendingPayments(payments.stream()
                .filter(p -> p.getStatus() == PaymentStatus.PENDING).count());
        summary.setTotalAmountSent(totalSent != null ? totalSent : BigDecimal.ZERO);
        return summary;
    }

    private PaymentDTO.Response toResponse(Payment payment) {
        PaymentDTO.Response response = new PaymentDTO.Response();
        response.setId(payment.getId());
        response.setSenderId(payment.getSenderId());
        response.setReceiverId(payment.getReceiverId());
        response.setAmount(payment.getAmount());
        response.setCurrency(payment.getCurrency());
        response.setStatus(payment.getStatus());
        response.setDescription(payment.getDescription());
        response.setCreatedAt(payment.getCreatedAt());
        response.setUpdatedAt(payment.getUpdatedAt());
        return response;
    }
}