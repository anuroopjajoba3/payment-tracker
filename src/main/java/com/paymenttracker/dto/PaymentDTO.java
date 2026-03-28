package com.paymenttracker.dto;

import com.paymenttracker.model.PaymentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PaymentDTO {

    public static class CreateRequest {
        @NotBlank(message = "Sender ID is required")
        private String senderId;
        @NotBlank(message = "Receiver ID is required")
        private String receiverId;
        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be positive")
        private BigDecimal amount;
        @NotBlank(message = "Currency is required")
        private String currency;
        private String description;

        public String getSenderId() { return senderId; }
        public String getReceiverId() { return receiverId; }
        public BigDecimal getAmount() { return amount; }
        public String getCurrency() { return currency; }
        public String getDescription() { return description; }
        public void setSenderId(String s) { this.senderId = s; }
        public void setReceiverId(String r) { this.receiverId = r; }
        public void setAmount(BigDecimal a) { this.amount = a; }
        public void setCurrency(String c) { this.currency = c; }
        public void setDescription(String d) { this.description = d; }
    }

    public static class UpdateStatusRequest {
        @NotNull(message = "Status is required")
        private PaymentStatus status;
        public PaymentStatus getStatus() { return status; }
        public void setStatus(PaymentStatus s) { this.status = s; }
    }

    public static class Response {
        private Long id;
        private String senderId;
        private String receiverId;
        private BigDecimal amount;
        private String currency;
        private PaymentStatus status;
        private String description;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public Long getId() { return id; }
        public String getSenderId() { return senderId; }
        public String getReceiverId() { return receiverId; }
        public BigDecimal getAmount() { return amount; }
        public String getCurrency() { return currency; }
        public PaymentStatus getStatus() { return status; }
        public String getDescription() { return description; }
        public LocalDateTime getCreatedAt() { return createdAt; }
        public LocalDateTime getUpdatedAt() { return updatedAt; }
        public void setId(Long id) { this.id = id; }
        public void setSenderId(String s) { this.senderId = s; }
        public void setReceiverId(String r) { this.receiverId = r; }
        public void setAmount(BigDecimal a) { this.amount = a; }
        public void setCurrency(String c) { this.currency = c; }
        public void setStatus(PaymentStatus s) { this.status = s; }
        public void setDescription(String d) { this.description = d; }
        public void setCreatedAt(LocalDateTime t) { this.createdAt = t; }
        public void setUpdatedAt(LocalDateTime t) { this.updatedAt = t; }
    }

    public static class SummaryResponse {
        private String senderId;
        private long totalPayments;
        private long completedPayments;
        private long pendingPayments;
        private BigDecimal totalAmountSent;

        public String getSenderId() { return senderId; }
        public long getTotalPayments() { return totalPayments; }
        public long getCompletedPayments() { return completedPayments; }
        public long getPendingPayments() { return pendingPayments; }
        public BigDecimal getTotalAmountSent() { return totalAmountSent; }
        public void setSenderId(String s) { this.senderId = s; }
        public void setTotalPayments(long t) { this.totalPayments = t; }
        public void setCompletedPayments(long c) { this.completedPayments = c; }
        public void setPendingPayments(long p) { this.pendingPayments = p; }
        public void setTotalAmountSent(BigDecimal t) { this.totalAmountSent = t; }
    }
}
