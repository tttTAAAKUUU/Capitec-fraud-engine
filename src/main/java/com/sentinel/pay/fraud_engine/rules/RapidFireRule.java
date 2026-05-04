package com.sentinel.pay.fraud_engine.rules;

import com.sentinel.pay.fraud_engine.model.Transaction;
import com.sentinel.pay.fraud_engine.repository.TransactionRepository;
import org.springframework.stereotype.Component;

@Component
public class RapidFireRule implements FraudRule {
    private final TransactionRepository txRepo;

    public RapidFireRule(TransactionRepository txRepo) { this.txRepo = txRepo; }

    @Override
    public boolean isSuspicious(Transaction tx) {
        return txRepo.findFirstByAccountNumberOrderByTimestampDesc(tx.getAccountNumber())
                .map(lastTx -> {
                    if (tx.getId() != null && tx.getId().equals(lastTx.getId())) return false;

                    return tx.getTimestamp().isBefore(lastTx.getTimestamp().plusSeconds(30));
                })
                .orElse(false);
    }

    @Override
    public String getReason() { return "Multiple transactions attempted in too short a timeframe."; }

    @Override
    public String getSeverity() {
        return "HIGH";
    }
}