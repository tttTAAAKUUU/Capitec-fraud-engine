package com.sentinel.pay.fraud_engine.repository;

import com.sentinel.pay.fraud_engine.model.AccountProfile;
import com.sentinel.pay.fraud_engine.model.FraudAlert;
import com.sentinel.pay.fraud_engine.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface    AccountProfileRepository extends JpaRepository<AccountProfile, Long> {
    Optional<AccountProfile> findByAccountNumber(String accountNumber);
}