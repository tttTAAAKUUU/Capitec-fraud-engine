package com.sentinel.pay.fraud_engine.service;

import com.sentinel.pay.fraud_engine.model.FraudAlert;
import com.sentinel.pay.fraud_engine.model.Transaction;
import com.sentinel.pay.fraud_engine.repository.TransactionRepository;
import com.sentinel.pay.fraud_engine.repository.FraudAlertRepository;
import com.sentinel.pay.fraud_engine.rules.FraudRule;
import org.springframework.context.annotation.Lazy; // Add this import
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class FraudService {

    private final TransactionRepository transactionRepo;
    private final FraudAlertRepository alertRepo;
    private final List<FraudRule> rules;

    public FraudService(TransactionRepository transactionRepo,
                        FraudAlertRepository alertRepo,
                        @Lazy List<FraudRule> rules) {
        this.transactionRepo = transactionRepo;
        this.alertRepo = alertRepo;
        this.rules = rules;
    }

    public Transaction validate(Transaction tx) {
        tx.setStatus("CLEAN");
        // Ensure timestamp is set BEFORE rules check it
        if (tx.getTimestamp() == null) {
            tx.setTimestamp(LocalDateTime.now());
        }

        List<FraudRule> triggeredRules = new ArrayList<>();
        for (FraudRule rule : rules) {
            if (rule.isSuspicious(tx)) {
                triggeredRules.add(rule);
            }
        }

        // Save first to get the ID for the alerts
        Transaction savedTx = transactionRepo.save(tx);

        if (!triggeredRules.isEmpty()) {
            savedTx.setStatus("FLAGGED");
            for (FraudRule rule : triggeredRules) {
                FraudAlert alert = new FraudAlert();
                alert.setTransactionId(savedTx.getId());
                alert.setReason(rule.getReason());
                alert.setSeverity(rule.getSeverity());
                alert.setCreatedAt(LocalDateTime.now());
                alertRepo.save(alert);
            }
            return transactionRepo.save(savedTx);
        }
        return savedTx;
    }
}