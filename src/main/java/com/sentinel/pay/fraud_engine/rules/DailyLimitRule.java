package com.sentinel.pay.fraud_engine.rules;

import com.sentinel.pay.fraud_engine.model.Transaction;
import com.sentinel.pay.fraud_engine.model.AccountProfile;
import com.sentinel.pay.fraud_engine.repository.TransactionRepository;
import com.sentinel.pay.fraud_engine.repository.AccountProfileRepository;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Component
public class DailyLimitRule implements FraudRule {

    private final TransactionRepository transactionRepo;
    private final AccountProfileRepository profileRepo;

    public DailyLimitRule(TransactionRepository transactionRepo, AccountProfileRepository profileRepo) {
        this.transactionRepo = transactionRepo;
        this.profileRepo = profileRepo;
    }

    @Override
    public boolean isSuspicious(Transaction transaction) {
        LocalDateTime startOfDay = LocalDateTime.now().with(LocalTime.MIN);

        List<Transaction> todayTransactions = transactionRepo.findByAccountNumberAndTimestampAfter(
                transaction.getAccountNumber(), startOfDay
        );

        double totalSpentToday = todayTransactions.stream()
                .mapToDouble(Transaction::getAmount)
                .sum();

        return profileRepo.findByAccountNumber(transaction.getAccountNumber())
                .map(profile -> {
                    double newTotal = totalSpentToday + transaction.getAmount();
                    return newTotal > profile.getDailyLimit();
                })
                .orElse(false);
    }

    @Override
    public String getReason() {
        return "Daily spending limit exceeded for this account.";
    }

    @Override
    public String getSeverity() {
        return "HIGH";
    }
}