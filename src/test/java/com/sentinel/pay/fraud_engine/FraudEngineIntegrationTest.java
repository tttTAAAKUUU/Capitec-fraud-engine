package com.sentinel.pay.fraud_engine;

import com.sentinel.pay.fraud_engine.model.AccountProfile;
import com.sentinel.pay.fraud_engine.model.Transaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FraudEngineIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testFullFraudLifecycle() {
        // 1. Create an account
        AccountProfile profile = new AccountProfile(null, "TEST-1", 1000.0, LocalDateTime.now());
        restTemplate.postForEntity("/api/accounts", profile, AccountProfile.class);

        // 2. Send a high-amount transaction
        Transaction tx = new Transaction();
        tx.setAccountNumber("TEST-1");
        tx.setAmount(12000.0);
        tx.setCountry("IRAN");
        tx.setCategory("CRYPTO");
        tx.setMerchantName("HighRisk-Exchange");
        tx.setTimestamp(LocalDateTime.now());

        ResponseEntity<Transaction> response = restTemplate.postForEntity(
                "/api/transactions/validate", tx, Transaction.class);

        // Ensure status is FLAGGED
        assertNotNull(response.getBody());
        assertEquals("FLAGGED", response.getBody().getStatus());

        // 3. Check if alerts were actually created
        Long txId = response.getBody().getId();
        ResponseEntity<List> alerts = restTemplate.getForEntity(
                "/api/transactions/" + txId + "/alerts", List.class);

        assertNotNull(alerts.getBody());
        assertTrue(alerts.getBody().size() >= 1, "Should have at least one fraud alert");
    }
}