package com.sentinel.pay.fraud_engine.rules;

import com.sentinel.pay.fraud_engine.model.Transaction;
import com.sentinel.pay.fraud_engine.repository.TransactionRepository;
import org.springframework.stereotype.Component;

@Component
public class ImpossibleTravelRule implements FraudRule {
    private final TransactionRepository txRepo;

    public ImpossibleTravelRule(TransactionRepository txRepo) { this.txRepo = txRepo; }

    @Override
    public boolean isSuspicious(Transaction tx) {
        return txRepo.findFirstByAccountNumberOrderByTimestampDesc(tx.getAccountNumber())
                .map(lastTx -> {
                    if (tx.getId() != null && tx.getId().equals(lastTx.getId())) return false;

                    boolean differentLocation = !lastTx.getCountry().equalsIgnoreCase(tx.getCountry());
                    boolean withinTwoHours = tx.getTimestamp().isBefore(lastTx.getTimestamp().plusHours(2));
                    return differentLocation && withinTwoHours;
                }).orElse(false);
    }

    @Override
    public String getReason() { return "Impossible travel detected: location changed too quickly."; }

    @Override
    public String getSeverity() {
        return "CRITICAL";
    }
}