package com.sentinel.pay.fraud_engine.controller;

import com.sentinel.pay.fraud_engine.model.FraudAlert;
import com.sentinel.pay.fraud_engine.model.Transaction;
import com.sentinel.pay.fraud_engine.repository.FraudAlertRepository;
import com.sentinel.pay.fraud_engine.repository.TransactionRepository; // Fix: Add this import
import com.sentinel.pay.fraud_engine.service.FraudService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    private final FraudService fraudService;
    private final FraudAlertRepository alertRepo;
    private final TransactionRepository transactionRepo;

    public TransactionController(FraudService fraudService, FraudAlertRepository alertRepo, TransactionRepository transactionRepo){
        this.fraudService = fraudService;
        this.alertRepo = alertRepo;
        this.transactionRepo = transactionRepo;
    }

    @PostMapping("/validate")
    public ResponseEntity<Transaction> validate(@RequestBody Transaction tx) {
        return ResponseEntity.ok(fraudService.validate(tx));
    }

    @GetMapping("/account/{accNum}")
    public ResponseEntity<List<Transaction>> getByAccount(@PathVariable String accNum) {
        return ResponseEntity.ok(transactionRepo.findByAccountNumber(accNum));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getById(@PathVariable Long id) {
        return transactionRepo.findById(id)
                .map(tx -> ResponseEntity.ok(tx))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/alerts")
    public ResponseEntity<List<FraudAlert>> getAlerts(@PathVariable Long id) {
        return ResponseEntity.ok(alertRepo.findByTransactionId(id));
    }

    @GetMapping("/account/{accNum}/flagged")
    public ResponseEntity<List<Transaction>> getFlaggedByAccount(@PathVariable String accNum) {
        List<Transaction> flagged = transactionRepo.findByAccountNumber(accNum).stream()
                .filter(tx -> "FLAGGED".equals(tx.getStatus()))
                .toList();
        return ResponseEntity.ok(flagged);
    }

    @GetMapping("/account/{accNum}/alerts")
    public ResponseEntity<List<FraudAlert>> getAccountAlerts(@PathVariable String accNum) {
        List<Long> txIds = transactionRepo.findByAccountNumber(accNum).stream()
                .map(Transaction::getId)
                .toList();

        return ResponseEntity.ok(alertRepo.findAllByTransactionIdIn(txIds));
    }
}