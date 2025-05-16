# Spring Boot Customer Management API

## Overview
A modern, production-ready RESTful API for managing customer data, built with Java Spring Boot and H2 in-memory database. The API supports full CRUD operations, on-the-fly membership tier calculation, OpenAPI documentation, and automated tests. Designed for rapid local development and easy validation.

---

## Requirements Checklist

- [x] **Spring Boot** RESTful API with Maven
- [x] **H2** in-memory database for local testing
- [x] **Customer model**: id (UUID), name (required), email (required, validated), annualSpend (optional), lastPurchaseDate (optional, ISO 8601)
- [x] **CRUD endpoints**:
  - [x] `POST   /customer` (create, no id in request, id in response)
  - [x] `GET    /customer/{id}` (retrieve by id)
  - [x] `GET    /customer?name={name}` (retrieve by name)
  - [x] `GET    /customer?email={email}` (retrieve by email)
  - [x] `PUT    /customer/{id}` (update by id, all fields except id)
  - [x] `DELETE /customer/{id}` (delete by id)
- [x] **Business logic**: tier (Silver, Gold, Platinum) calculated on-the-fly, not stored
- [x] **OpenAPI spec** (`openapi.yaml`) with all endpoints, schemas, and error cases
- [x] **Unit/integration tests** for CRUD, tier logic, and validation
- [x] **README** with build/run instructions, sample requests, H2 console, and assumptions
- [x] **Automation**: `test_script.sh` validates all endpoints and business logic via curl

---

## Getting Started

### Build & Run

```sh
./mvnw spring-boot:run
```

### Automated API Test

Run all endpoint and business logic tests automatically:
```sh
./test_script.sh
```

---

## API Endpoints

- **POST   /customer**: Create customer (no `id` in request)
- **GET    /customer/{id}**: Get by ID
- **GET    /customer?name={name}**: Get by name
- **GET    /customer?email={email}**: Get by email
- **PUT    /customer/{id}**: Update by ID
- **DELETE /customer/{id}**: Delete by ID

### Sample Requests

**Create:**
```sh
curl -X POST http://localhost:8080/customer \
  -H 'Content-Type: application/json' \
  -d '{"name":"John Doe","email":"john@example.com","annualSpend":1200,"lastPurchaseDate":"2025-05-01T00:00:00"}'
```

**Get by ID:**
```sh
curl http://localhost:8080/customer/<uuid>
```

**Update:**
```sh
curl -X PUT http://localhost:8080/customer/<uuid> \
  -H 'Content-Type: application/json' \
  -d '{"name":"Jane Doe","email":"jane@example.com","annualSpend":15000,"lastPurchaseDate":"2025-04-01T00:00:00"}'
```

**Delete:**
```sh
curl -X DELETE http://localhost:8080/customer/<uuid>
```

---

## Business Logic: Tier Calculation
- **Silver**: annualSpend < $1000
- **Gold**: $1000 ≤ annualSpend < $10000 and purchased within last 12 months
- **Platinum**: annualSpend ≥ $10000 and purchased within last 6 months
- _Tier is calculated dynamically and not stored in the database._

---

## H2 Console
- URL: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- JDBC URL: `jdbc:h2:mem:testdb`

---

## Swagger UI / OpenAPI
- URL: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- Interactive API documentation and testing for all endpoints.
- OpenAPI YAML: See [`openapi.yaml`](./openapi.yaml) in project root.
- Swagger UI is enabled via springdoc-openapi dependency and auto-configured for this project.

---

## Assumptions
- Email must be unique and in a valid format.
- `annualSpend` and `lastPurchaseDate` are optional.
- Tier is always calculated on-the-fly.

---

## Contribution & License
This project is for coding challenge demonstration purposes. For questions or improvements, open an issue or PR.
