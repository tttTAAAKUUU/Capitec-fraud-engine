package com.sentinel.pay.fraud_engine.repository;

import com.sentinel.pay.fraud_engine.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccountNumber(String accountNumber);
    Optional<Transaction> findFirstByAccountNumberOrderByTimestampDesc(String accountNumber);
    List<Transaction> findByAccountNumberAndTimestampAfter(String accountNumber, LocalDateTime time);
}