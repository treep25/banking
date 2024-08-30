
# Banking Solution

## Description

Provided in task

## Technology Stack

- **Programming Language:** Java 17
- **Build Tool:** Maven
- **Database:** H2 (In-Memory)
- **Testing:** JUnit 5, Mockito
- **Test Coverage:** Jacoco

## Features

### Account Management
- Create a new account with an initial balance.
- Retrieve account information by account number.
- Retrieve a list of all accounts.

### Transactions
- Deposit funds into an account.
- Withdraw funds from an account.
- Transfer funds between two accounts.

## API Endpoints

### Account Management

1. **Create Account:**
   - **Method:** POST
   - **URL:** `/api/v1/accounts`
   - **Request Body:**
     ```json
     {
       "accountNumber": "123456",
       "initialBalance": 1000.00
     }
     ```
   - **Response:** `201 Created`
     ```json
     {
       "id": 1,
       "accountNumber": "123456",
       "balance": 1000.00
     }
     ```

2. **Get Account Information:**
   - **Method:** GET
   - **URL:** `/api/v1/accounts/{accountNumber}`
   - **Response:** `200 OK`
     ```json
     {
       "id": 1,
       "accountNumber": "123456",
       "balance": 1000.00
     }
     ```

3. **Get All Accounts:**
   - **Method:** GET
   - **URL:** `/api/v1/accounts`
   - **Response:** `200 OK`
     ```json
     [
       {
         "id": 1,
         "accountNumber": "123456",
         "balance": 1000.00
       },
       {
         "id": 2,
         "accountNumber": "654321",
         "balance": 500.00
       }
     ]
     ```

### Transactions

1. **Deposit Funds:**
   - **Method:** POST
   - **URL:** `/api/v1/transactions/deposit`
   - **Request Body:**
     ```json
     {
       "toAccount": "123456",
       "amount": 500.00
     }
     ```
   - **Response:** `200 OK`
     ```json
     {
       "message": "Deposit successful"
     }
     ```

2. **Withdraw Funds:**
   - **Method:** POST
   - **URL:** `/api/v1/transactions/withdraw`
   - **Request Body:**
     ```json
     {
       "fromAccount": "123456",
       "amount": 200.00
     }
     ```
   - **Response:** `200 OK`
     ```json
     {
       "message": "Withdrawal successful"
     }
     ```

3. **Transfer Funds:**
   - **Method:** POST
   - **URL:** `/api/v1/transactions/transfer`
   - **Request Body:**
     ```json
     {
       "fromAccount": "123456",
       "toAccount": "654321",
       "amount": 300.00
     }
     ```
   - **Response:** `200 OK`
     ```json
     {
       "message": "Transfer successful"
     }
     ```

## Setup Instructions

### Requirements

- **Java 17**
- **Maven 3.x**

### Steps to Run the Application

1. **Build the project using Maven:**
   ```bash
   mvn clean install
   ```

2. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

3. **The application will be available at:**
   ```
   http://localhost:8080
   ```

### Postman collections

is in root directory

### Running Tests

To run the tests, use the following command:
```bash
mvn test
```

### Generating Test Coverage Report

The test coverage report is generated using Jacoco. To generate the report, run:
```bash
mvn jacoco:report
```

The report can be found in the `target/site/jacoco/` directory.

## Database Usage

The application uses an embedded in-memory H2 database, which is initialized on application startup. The database will reset on every application restart.
