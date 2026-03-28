package com.paymenttracker.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String senderId;

    @Column(nullable = false)
    private String receiverId;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @Column
    private String description;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) status = PaymentStatus.PENDING;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Getters
    public Long getId() { return id; }
    public String getSenderId() { return senderId; }
    public String getReceiverId() { return receiverId; }
    public BigDecimal getAmount() { return amount; }
    public String getCurrency() { return currency; }
    public PaymentStatus getStatus() { return status; }
    public String getDescription() { return description; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setSenderId(String senderId) { this.senderId = senderId; }
    public void setReceiverId(String receiverId) { this.receiverId = receiverId; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public void setCurrency(String currency) { this.currency = currency; }
    public void setStatus(PaymentStatus status) { this.status = status; }
    public void setDescription(String description) { this.description = description; }

    // Builder
    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private final Payment payment = new Payment();
        public Builder id(Long id) { payment.id = id; return this; }
        public Builder senderId(String senderId) { payment.senderId = senderId; return this; }
        public Builder receiverId(String receiverId) { payment.receiverId = receiverId; return this; }
        public Builder amount(BigDecimal amount) { payment.amount = amount; return this; }
        public Builder currency(String currency) { payment.currency = currency; return this; }
        public Builder status(PaymentStatus status) { payment.status = status; return this; }
        public Builder description(String description) { payment.description = description; return this; }
        public Builder createdAt(LocalDateTime createdAt) { payment.createdAt = createdAt; return this; }
        public Builder updatedAt(LocalDateTime updatedAt) { payment.updatedAt = updatedAt; return this; }
        public Payment build() { return payment; }
    }
}
