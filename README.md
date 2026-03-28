# PaymentTracker API

A secure B2B payment tracking REST API built with Java 17, Spring Boot 3, Spring Data JPA, and PostgreSQL.

## Tech Stack

- **Java 17** + **Spring Boot 3.2**
- **Spring Data JPA** — ORM and database access
- **Spring Security** — JWT-based authentication
- **PostgreSQL** — production database
- **H2** — in-memory database for tests
- **JUnit 5 + Mockito** — unit and integration testing
- **Docker + Docker Compose** — containerization
- **GitHub Actions** — CI/CD pipeline

## Features

- Create, read, update, and delete payments
- Track payment status (PENDING → PROCESSING → COMPLETED/FAILED/CANCELLED)
- Filter payments by sender, receiver, or status
- Get sender payment summary with total amounts
- JWT-based authentication
- Input validation with meaningful error messages
- Global exception handling

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/payments` | Create a new payment |
| GET | `/api/v1/payments` | Get all payments |
| GET | `/api/v1/payments/{id}` | Get payment by ID |
| GET | `/api/v1/payments?senderId={id}` | Get payments by sender |
| GET | `/api/v1/payments?status={status}` | Get payments by status |
| PATCH | `/api/v1/payments/{id}/status` | Update payment status |
| DELETE | `/api/v1/payments/{id}` | Delete payment |
| GET | `/api/v1/payments/summary/{senderId}` | Get sender summary |

## Running Locally
```bash
# Start PostgreSQL and app with Docker Compose
docker-compose up

# Or run locally with Maven
mvn spring-boot:run

# Run tests
mvn test
```

## Sample Request
```json
POST /api/v1/payments
{
  "senderId": "company-001",
  "receiverId": "vendor-042",
  "amount": 15000.00,
  "currency": "USD",
  "description": "Q1 software license payment"
}
```

Built by [Anuroop Jajoba](https://github.com/anuroopjajoba3)