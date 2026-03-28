package com.paymenttracker.service;

import com.paymenttracker.dto.PaymentDTO;
import com.paymenttracker.exception.PaymentNotFoundException;
import com.paymenttracker.model.Payment;
import com.paymenttracker.model.PaymentStatus;
import com.paymenttracker.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    private Payment mockPayment;

    @BeforeEach
    void setUp() {
        mockPayment = Payment.builder()
                .id(1L)
                .senderId("sender-001")
                .receiverId("receiver-001")
                .amount(new BigDecimal("500.00"))
                .currency("USD")
                .status(PaymentStatus.PENDING)
                .description("Test payment")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    @Test
    void createPayment_ShouldReturnCreatedPayment() {
        PaymentDTO.CreateRequest request = new PaymentDTO.CreateRequest();
        request.setSenderId("sender-001");
        request.setReceiverId("receiver-001");
        request.setAmount(new BigDecimal("500.00"));
        request.setCurrency("USD");
        request.setDescription("Test payment");

        when(paymentRepository.save(any(Payment.class))).thenReturn(mockPayment);

        PaymentDTO.Response response = paymentService.createPayment(request);

        assertThat(response).isNotNull();
        assertThat(response.getSenderId()).isEqualTo("sender-001");
        assertThat(response.getAmount()).isEqualByComparingTo("500.00");
        assertThat(response.getStatus()).isEqualTo(PaymentStatus.PENDING);
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void getPaymentById_WhenExists_ShouldReturnPayment() {
        when(paymentRepository.findById(1L)).thenReturn(Optional.of(mockPayment));

        PaymentDTO.Response response = paymentService.getPaymentById(1L);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getSenderId()).isEqualTo("sender-001");
    }

    @Test
    void getPaymentById_WhenNotExists_ShouldThrowException() {
        when(paymentRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> paymentService.getPaymentById(999L))
                .isInstanceOf(PaymentNotFoundException.class)
                .hasMessageContaining("Payment not found with id: 999");
    }

    @Test
    void updatePaymentStatus_ShouldUpdateAndReturnPayment() {
        PaymentDTO.UpdateStatusRequest request = new PaymentDTO.UpdateStatusRequest();
        request.setStatus(PaymentStatus.COMPLETED);

        Payment updatedPayment = Payment.builder()
                .id(1L)
                .senderId("sender-001")
                .receiverId("receiver-001")
                .amount(new BigDecimal("500.00"))
                .currency("USD")
                .status(PaymentStatus.COMPLETED)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        when(paymentRepository.findById(1L)).thenReturn(Optional.of(mockPayment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(updatedPayment);

        PaymentDTO.Response response = paymentService.updatePaymentStatus(1L, request);

        assertThat(response.getStatus()).isEqualTo(PaymentStatus.COMPLETED);
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void deletePayment_WhenExists_ShouldDelete() {
        when(paymentRepository.existsById(1L)).thenReturn(true);

        paymentService.deletePayment(1L);

        verify(paymentRepository, times(1)).deleteById(1L);
    }

    @Test
    void deletePayment_WhenNotExists_ShouldThrowException() {
        when(paymentRepository.existsById(999L)).thenReturn(false);

        assertThatThrownBy(() -> paymentService.deletePayment(999L))
                .isInstanceOf(PaymentNotFoundException.class);
    }

    @Test
    void getPaymentsBySender_ShouldReturnFilteredPayments() {
        when(paymentRepository.findBySenderId("sender-001")).thenReturn(List.of(mockPayment));

        List<PaymentDTO.Response> responses = paymentService.getPaymentsBySender("sender-001");

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).getSenderId()).isEqualTo("sender-001");
    }
}