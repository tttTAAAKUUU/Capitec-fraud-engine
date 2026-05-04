package com.sentinel.pay.fraud_engine.controller;

import com.sentinel.pay.fraud_engine.model.AccountProfile;
import com.sentinel.pay.fraud_engine.repository.AccountProfileRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountProfileController {
    private final AccountProfileRepository profileRepo;

    public AccountProfileController(AccountProfileRepository profileRepo) {
        this.profileRepo = profileRepo;
    }

    @PostMapping
    public ResponseEntity<AccountProfile> createAccount(@RequestBody AccountProfile profile) {
        return ResponseEntity.ok(profileRepo.save(profile));
    }

    @GetMapping("/{accNum}")
    public ResponseEntity<AccountProfile> getAccount(@PathVariable String accNum) {
        return profileRepo.findByAccountNumber(accNum)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}