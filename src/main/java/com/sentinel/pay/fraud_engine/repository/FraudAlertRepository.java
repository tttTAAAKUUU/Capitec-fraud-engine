package com.sentinel.pay.fraud_engine.repository;

import com.sentinel.pay.fraud_engine.model.FraudAlert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FraudAlertRepository extends JpaRepository<FraudAlert, Long> {
    List<FraudAlert> findByTransactionId(Long transactionId);
    List<FraudAlert> findAllByTransactionIdIn(List<Long> transactionIds);
}