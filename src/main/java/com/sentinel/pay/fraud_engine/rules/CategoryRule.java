package com.sentinel.pay.fraud_engine.rules;

import com.sentinel.pay.fraud_engine.model.Transaction;
import org.springframework.stereotype.Component;
import java.util.Set;

@Component
public class CategoryRule implements FraudRule {

    private static final Set<String> RISKY_CATEGORIES = Set.of(
            "CRYPTO", "CASINO", "GAMBLING", "ADULT", "WEAPONS"
    );

    @Override
    public boolean isSuspicious(Transaction transaction) {
        if (transaction.getCategory() == null) return false;
        return RISKY_CATEGORIES.contains(transaction.getCategory().toUpperCase());
    }

    @Override
    public String getReason() {
        return "Transaction involving a high-risk industry category.";
    }

    @Override
    public String getSeverity() {
        return "MEDIUM";
    }
}