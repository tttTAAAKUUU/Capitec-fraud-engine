package com.sentinel.pay.fraud_engine.rules;

import com.sentinel.pay.fraud_engine.model.Transaction;
import org.springframework.stereotype.Component;

@Component
public class HighAmountRule implements FraudRule {

    @Override
    public boolean isSuspicious(Transaction transaction) {
        return transaction.getAmount() > 10000;
    }

    @Override
    public String getReason() {
        return "Transaction amount exceeds the R10,000 safety limit.";
    }

    @Override
    public String getSeverity() {
        return "MEDIUM";
    }
}