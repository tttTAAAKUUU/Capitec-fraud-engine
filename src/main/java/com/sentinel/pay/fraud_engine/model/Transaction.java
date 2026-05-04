package com.sentinel.pay.fraud_engine.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNumber;
    private Double amount;
    private String category;
    private String country;
    private String merchantName;
    private LocalDateTime timestamp;
    private String status;

    public Transaction() {}

    public Transaction(Long id, String accountNumber, Double amount, String category,
                       String country, String merchantName, LocalDateTime timestamp, String status) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.category = category;
        this.country = country;
        this.merchantName = merchantName;
        this.timestamp = timestamp;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public Double getAmount() { return amount; }
    public void setAmount(Double amount) { this.amount = amount; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getMerchantName() { return merchantName; }
    public void setMerchantName(String merchantName) { this.merchantName = merchantName; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}