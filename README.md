# Tournament Management System

A robust, enterprise-grade backend for managing sports and gaming tournaments. Built with Spring Boot 3.4, Java 21, and PostgreSQL.

## 🚀 Overview

This system provides a comprehensive API to handle organizations, multi-format tournaments (Single Elimination, Round Robin), participant registration, and live match results. It features Role-Based Access Control (RBAC), automated bracket generation, and asynchronous auditing.

### Key Features
- **Authentication**: JWT-based security with access and refresh tokens.
- **Organization Management**: Multi-tenancy support with organizations and memberships.
- **Tournament Lifecycle**: DRAFT -> REGISTRATION -> IN_PROGRESS -> COMPLETED.
- **Bracket Support**:
  - **Single Elimination**: Automated pairing and winner advancement (basic).
  - **Round Robin**: Full league permutation generation.
- **Security**: Granular RBAC (ORG_ADMIN, TOURNAMENT_ADMIN, REFEREE, VIEWER).
- **Audit**: Asynchronous logging of all critical system actions into the database.
- **Application Logging**: Structured logging with SLF4J and Logback, including daily log rotation and colorized console output.

## 🛠 Tech Stack
- **Core**: Java 21, Spring Boot 3.4
- **Database**: PostgreSQL (JSONB for dynamic fields)
- **Migrations**: Liquibase
- **Mapping**: ModelMapper
- **Security**: Spring Security + JJWT
- **API Documentation**: Springdoc OpenAPI
- **Testing**: JUnit 5, Mockito, Testcontainers

## 🏁 Getting Started

### Prerequisites
- JDK 21
- Docker (for PostgreSQL and Testcontainers)
- Maven 3.9+

### Installation (Manual)
1. Clone the repository.
2. Configure your `application.yml` with your database credentials and JWT secret.
3. Run migrations and start the app:
   ```bash
   mvn spring-boot:run
   ```

### 🐳 Docker Deployment (Recommended)
You can start the entire stack (App + PostgreSQL) with a single command:
```bash
docker-compose up --build
```
This will:
- Build the optimized multi-stage image.
- Start a PostgreSQL 16 container with healthchecks.
- Start the application using the `docker` Spring profile.

Once started, the app will be reachable at `http://localhost:8080`.

### Documentation
Once the app is running, access the interactive API docs at:
`http://localhost:8080/swagger-ui.html`

## 🧪 Testing
The project includes a comprehensive test suite:
- **Unit Tests**: Coverage for all service logic and security rules.
- **Integration Tests**: (In Progress) End-to-end flows using Testcontainers.

Run tests with:
```bash
mvn clean verify
```

## 📖 Developer Guide
This project follows **Clean Code** principles and **SOLID** design patterns.
- **Controllers**: Thin layers for request/response handling and Swagger definitions.
- **Services**: Contain business logic and are annotated with JavaDocs for clarity.
- **Domain**: Rich entities using JPA and Lombok.
- **Security**: Logic is centralized in `SecurityService` for maintainability.
- **Logging**: Uses `@Slf4j` for technical logs (errors, info) and a custom `AuditService` for business-level auditing. Logs are stored in `./logs` with a 30-day retention policy.

---
*Developed as part of the Tournament Management System modernization initiative.*
