package com.sentinel.pay.fraud_engine.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "account_profiles")
public class AccountProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String accountNumber;
    private double dailyLimit;
    private LocalDateTime lastTransactionDate;

    public AccountProfile() {}

    public AccountProfile(Long id, String accountNumber, double dailyLimit, LocalDateTime lastTransactionDate) {
        this.id = id;
        this.accountNumber = accountNumber;
        this.dailyLimit = dailyLimit;
        this.lastTransactionDate = lastTransactionDate;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }

    public double getDailyLimit() { return dailyLimit; }
    public void setDailyLimit(double dailyLimit) { this.dailyLimit = dailyLimit; }

    public LocalDateTime getLastTransactionDate() { return lastTransactionDate; }
    public void setLastTransactionDate(LocalDateTime lastTransactionDate) { this.lastTransactionDate = lastTransactionDate; }
}