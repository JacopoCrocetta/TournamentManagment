# Tournament Management System

A robust, enterprise-grade backend for managing sports and gaming tournaments. Built with Spring Boot 3.4, Java 21, and PostgreSQL.

## 🚀 Overview

This system provides a comprehensive API to handle organizations, multi-format tournaments, participant registration, check-ins, live match results, formatting algorithms, and dispute resolution. It features Role-Based Access Control (RBAC), automated bracket generation, and asynchronous auditing.

### Key Features
- **Authentication & Security**: JWT-based security with access/refresh tokens. Granular RBAC (ORG_ADMIN, TOURNAMENT_ADMIN, REFEREE, VIEWER) managed through `SecurityService` traversing from Organization down to Match level.
- **Organization Management**: Multi-tenancy support with organizations and memberships.
- **Tournament Lifecycle**: Comprehensive flow from DRAFT -> REGISTRATION_OPEN -> REGISTRATION_CLOSED -> IN_PROGRESS -> COMPLETED.
- **Participant Workflows**: Pre-tournament Check-in system, Rating-based seeding, Waitlist management.
- **Advanced Match Formats**:
  - **Single Elimination**: Automated pairing, dynamic advancement of winners to next stages.
  - **Double Elimination**: Advanced logic managing Winner (W) and Loser (L) brackets. Drops losers into the L-bracket and converges for the Grand Finals.
  - **Round Robin**: Full league permutation matrix generation with advanced Tie-Breakers (Head-to-Head, Points Difference) for final standings.
  - **Seeding Policies**: Supports `RANDOM`, `MANUAL`, and `RATING` based seeding.
- **Live Match Mechanics**:
  - Best-of-X Series support.
  - Forfeit and No-Show handling.
  - Dispute resolution workflow (Opening disputes, resolution by TOURNAMENT_ADMIN/REFEREE).
- **Audit Logging**: Asynchronous logging of all critical system actions using Spring `@Async`.
- **Application Logging**: Structured logging with SLF4J and Logback with daily log rotation.

## 🛠 Tech Stack
- **Core**: Java 21, Spring Boot 3.4
- **Database**: PostgreSQL (JSONB for dynamic fields)
- **Migrations**: Liquibase
- **Mapping**: MapStruct (Automated, type-safe)
- **Real-time**: Spring WebSocket + STOMP
- **Security**: Spring Security 6 + JJWT
- **API Documentation**: Springdoc OpenAPI
- **Testing**: JUnit 5, Mockito, Testcontainers

## 🏁 Getting Started

### Prerequisites
- **JDK 21**
- **Docker** (for PostgreSQL)
- **Maven 3.9+**

### ⚡ Quick Start (Docker Compose)
The easiest way to start the entire stack (App + PostgreSQL) is using Docker Compose:

```bash
docker-compose up --build
```

This command builds the optimized multi-stage image and starts a PostgreSQL 16 container. The application will be available at `http://localhost:8080`.

### 🛠 Manual Installation & Development
If you prefer to run the application manually for development:

1. **Start the Database**:
   Ensure you have a PostgreSQL instance running. You can start a local one with:
   ```bash
   docker run --name tournament-db -e POSTGRES_DB=tournament_db -e POSTGRES_PASSWORD=postgres -p 5432:5432 -d postgres:16
   ```

2. **Configure Environment (Optional)**:
   The application uses sensible defaults in `application.properties`. You can override them via environment variables:
   - `DB_URL`: `jdbc:postgresql://localhost:5432/tournament_db`
   - `DB_USER`: `postgres`
   - `DB_PASS`: `postgres`

3. **Build and Run**:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

### 📖 API Documentation
Once the app is running, access the interactive Swagger UI at:
👉 **`http://localhost:8080/swagger-ui.html`**

## 🧪 Testing
The project includes a comprehensive test suite covering business logic and boundary rules.
- **Unit Tests**: Coverage for all service logic, scoring matrices, tie-breakers, and security rules.
- **Integration Tests**: End-to-end flows using PostgreSQL via Testcontainers testing controllers, security chains, and database integrity.

Run tests with:
```bash
mvn clean verify
```

## 📖 Developer Guide
This project follows **Clean Code** principles and **SOLID** design patterns.
- **Controllers**: Thin layers for request/response handling, documented with OpenApi/Swagger annotations.
- **Services**: Contain business logic and cleanly handle operations via custom domain-specific Exceptions (`BusinessRuleViolationException`, `InvalidStateTransitionException`, etc.).
- **Domain**: Rich JPA entities with clear lifecycle boundaries and optimized querying logic.
- **Bracket Strategies**: Implemented via Strategy Pattern. Open-closed for custom formats.
- **Auditing**: Uses `@Slf4j` for technical logs and a custom `AuditService` for domain actions, kept for 30-day retention policies.

---
*Developed as part of the Tournament Management System modernization and feature-enrichment initiative.*
