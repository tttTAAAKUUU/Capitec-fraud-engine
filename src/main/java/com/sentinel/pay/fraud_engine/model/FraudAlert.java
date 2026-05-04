package com.sentinel.pay.fraud_engine.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "fraud_alerts")
public class FraudAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long transactionId;
    private String reason;
    private String severity;
    private LocalDateTime createdAt;

    public FraudAlert() {}

    public FraudAlert(Long id, Long transactionId, String reason, String severity, LocalDateTime createdAt) {
        this.id = id;
        this.transactionId = transactionId;
        this.reason = reason;
        this.severity = severity;
        this.createdAt = createdAt;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getTransactionId() { return transactionId; }
    public void setTransactionId(Long transactionId) { this.transactionId = transactionId; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public String getSeverity() { return severity; }
    public void setSeverity(String severity) { this.severity = severity; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}