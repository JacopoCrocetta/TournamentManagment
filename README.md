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
- **Core**: Java 26, Spring Boot 3.4
- **Database**: PostgreSQL (JSONB for dynamic fields)
- **Migrations**: Liquibase
- **Mapping**: Manual Domain Mappers (Type-safe)
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
- **Bracket Engines**: Implemented via Strategy Pattern (`BracketEngine`). Expandable for Custom formats.
- **Auditing**: Uses `@Slf4j` for technical logs and a custom `AuditService` for domain actions, kept for 30-day retention policies.

---
*Developed as part of the Tournament Management System modernization and feature-enrichment initiative.*
