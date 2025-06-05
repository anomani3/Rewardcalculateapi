# Reward Points Calculation API

This is a Spring Boot REST API that calculates customer reward points based on recent transaction history. Points are awarded for transactions over $50 in the last 3 months.

## 🧩 Features

- Accepts transaction data with customer ID, amount, and date
- Calculates monthly and total reward points for each customer
- Filters out transactions older than 3 months
- Rejects transactions with future dates
- Gracefully handles missing customers
- Returns errors in JSON format for visibility in Postman

## 🛠 Technologies Used

- Java 23
- Spring Boot 3.5.1
- JUnit 5
- Maven
- REST APIs
- (Optional) PostgreSQL (if extended to persistence)

## 🎯 Reward Rules

| Amount Spent | Points Earned         |
|--------------|------------------------|
| <= $50       | 0 points               |
| $51–$100     | 1 point for each $ over $50 |
| > $100       | 2 points for each $ over $100 + 50 points |

**Example:**  
A transaction of $120 gives:  
- 50 points for $51–$100  
- 40 points for $101–$120  
→ Total = 90 points

---

## 🚀 API Endpoints

### ➕ Add Transaction

`POST /api/transactions`

**Request Body**
```json
{
  "customerId": "CUST001",
  "amount": 120.0,
  "date": "2025-06-01"
}


Responses

201 Created if added

400 Bad Request if the date is in the future

Responses

201 Created if added

400 Bad Request if the date is in the future


Response:
{
  "customerId": "CUST001",
  "monthlyPoints": {
    "2025-05": 90,
    "2025-04": 25,
    "2025-03": 250
  },
  "totalPoints": 365
}


Errors:

404 Not Found if customer doesn't exist


🏁 Run the App
./mvnw spring-boot:run



🔧 Future Improvements
Persist transactions in a database (PostgreSQL/MySQL)

Add customer registration and lookup

Implement pagination and filtering









