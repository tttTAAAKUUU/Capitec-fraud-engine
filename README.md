Getting Started
----------------------------------------------------------------------------
To get the engine running on your machine, follow these three simple steps:

1. Build the Image
Open your terminal in the project root and run:

Bash

docker build -t sentinel-fraud-engine .

2. Launch the Engine
Bash

docker run -p 8080:8080 sentinel-fraud-engine

4. Access the Database (Optional)
If you want to see the data moving in real-time, the H2 Console is available at:
URL: http://localhost:8080/h2-console

JDBC URL: jdbc:h2:mem:testdb

User: sa (No password)

How to Test the Engine (Step-by-Step)
----------------------------------------------------------------------------------
To see the fraud logic in action, you must run these commands in order.

Step 1: Create an Account Profile
Before the engine can check "historical" rules (like daily limits), an account must exist.

Why? The engine needs to know what the "normal" behavior or limits are for a specific user.

Bash

curl -X POST http://localhost:8080/api/accounts \
-H "Content-Type: application/json" \
-d '{
  "accountNumber": "ACC-777",
  "dailyLimit": 5000.0,
  "lastTransactionDate": "2026-05-04T09:00:00"
}'

Step 2: Validate a "Clean" Transaction
Send a small, normal transaction. The engine will check the rules and mark it as CLEAN.

Bash

curl -X POST http://localhost:8080/api/transactions/validate \
-H "Content-Type: application/json" \
-d '{
  "accountNumber": "ACC-777",
  "amount": 50.00,
  "merchantName": "Local Grocery",
  "category": "Food",
  "country": "ZA"
}'

Step 3: Trigger a "Fraud" Alert
Now, try to send a transaction that violates a rule (like a massive amount).

Bash

curl -X POST http://localhost:8080/api/transactions/validate \
-H "Content-Type: application/json" \
-d '{
  "accountNumber": "ACC-777",
  "amount": 150000.00,
  "merchantName": "Luxury Watches",
  "category": "Jewelry",
  "country": "Unknown"
}'
The Logic Behind the Curtain
----------------------------------------------------------------------------
The Entities: How they connect
AccountProfile: This represents the "User." It stores their historical behavior, such as their daily spending limit and the date of their last transaction.

Transaction: The actual event. It contains the "who, where, and how much."

FraudAlert: If a Transaction breaks a rule, a FraudAlert is created and linked to that Transaction ID, explaining why it was flagged.

The Rules & Logic
The engine follows a strict set of checks within the FraudService:

The High-Value Rule: Any transaction exceeding a certain threshold (e.g., $10,000) is automatically flagged. Even if the account is in good standing, high-value movement requires a second look.

The Velocity Rule (Historical Check): The engine looks at the AccountProfile. If the new transaction plus the total of today's previous transactions exceeds the dailyLimit, it is flagged.

The Location Rule: If a transaction originates from a "Country" marked as "Unknown" or high-risk, the severity of the alert is increased.

The Daily Limit Rule: Flags transactions that take a user over their limit

The Impossible travel rule: If 2 requests are made from 2 locations in too little time, the second will be flagged
