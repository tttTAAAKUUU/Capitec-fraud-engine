package com.sentinel.pay.fraud_engine.rules;

import com.sentinel.pay.fraud_engine.model.Transaction;
import com.sentinel.pay.fraud_engine.rules.FraudRule;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class HighRiskLocationRule implements FraudRule {
    private static final Set<String> BLOCKED_COUNTRIES = Set.of("NORTH_KOREA", "IRAN", "SYRIA");

    @Override
    public boolean isSuspicious(Transaction tx) {
        return BLOCKED_COUNTRIES.contains(tx.getCountry().toUpperCase());
    }

    @Override
    public String getReason() { return "Transaction from a high-risk sanctioned location."; }

    @Override
    public String getSeverity() {
        return "CRITICAL";
    }
}