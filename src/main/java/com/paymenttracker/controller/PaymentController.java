package com.paymenttracker.controller;

import com.paymenttracker.dto.PaymentDTO;
import com.paymenttracker.model.PaymentStatus;
import com.paymenttracker.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<PaymentDTO.Response> createPayment(@Valid @RequestBody PaymentDTO.CreateRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.createPayment(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDTO.Response> getPaymentById(@PathVariable Long id) {
        return ResponseEntity.ok(paymentService.getPaymentById(id));
    }

    @GetMapping
    public ResponseEntity<List<PaymentDTO.Response>> getAllPayments(
            @RequestParam(required = false) String senderId,
            @RequestParam(required = false) PaymentStatus status) {
        if (senderId != null) return ResponseEntity.ok(paymentService.getPaymentsBySender(senderId));
        if (status != null) return ResponseEntity.ok(paymentService.getPaymentsByStatus(status));
        return ResponseEntity.ok(paymentService.getAllPayments());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<PaymentDTO.Response> updatePaymentStatus(
            @PathVariable Long id, @Valid @RequestBody PaymentDTO.UpdateStatusRequest request) {
        return ResponseEntity.ok(paymentService.updatePaymentStatus(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Long id) {
        paymentService.deletePayment(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/summary/{senderId}")
    public ResponseEntity<PaymentDTO.SummaryResponse> getSenderSummary(@PathVariable String senderId) {
        return ResponseEntity.ok(paymentService.getSenderSummary(senderId));
    }
}
