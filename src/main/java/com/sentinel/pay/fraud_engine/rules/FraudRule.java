package com.sentinel.pay.fraud_engine.rules;

import com.sentinel.pay.fraud_engine.model.Transaction;

public interface FraudRule {
    boolean isSuspicious(Transaction transaction);
    String getReason();
    String getSeverity();
}