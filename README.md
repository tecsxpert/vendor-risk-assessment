# Vendor Risk Assessment System

A complete vendor risk assessment platform built with Spring Boot, PostgreSQL, Redis, Docker, and API documentation via Swagger. The system tracks vendor profiles, calculates risk scores, sends notification emails, caches frequently accessed data, and seeds demo data on startup.

## Overview

Vendor Risk Assessment enables organizations to manage third-party vendors, evaluate risk exposure, and monitor vendor health through automated scoring and reporting.

Key capabilities:
- Vendor profile management
- JWT-based authentication
- Risk score calculation and status classification
- Vendor risk report generation
- File upload/download support
- Email notifications for vendor events
- Audit logging and scheduled reminders
- Docker-based local development environment
- Swagger UI for API exploration

## Architecture

### ASCII Architecture Diagram

```
                                        +--------------------+
                                        |      Frontend      |
                                        |   React / Nginx    |
                                        |    (port 3000)     |
                                        +---------+----------+
                                                  |
                                                  |
                                                  v
                                      +--------------------------+
                                      |      Client Requests     |
                                      |   Swagger / REST API     |
                                      +-----------+--------------+
                                                  |
                                                  v
                              +-------------------[ Load Balancer ]-------------------+
                              |                                                    |
                              v                                                    v
                     +---------------------+                       +---------------------+
                     |    Backend API      |                       |  Background Jobs    |
                     |  Spring Boot App    |                       |  Scheduled Tasks    |
                     |  (port 8080)       |                       |                     |
                     +---------+-----------+                       +----------+----------+
                               |                                           |
         +---------------------+---------------------+                     |
         |                     |                     |                     |
         v                     v                     v                     v
+----------------+   +-------------------------+   +----------------+   +----------------+
| PostgreSQL DB  |   |     Redis Cache        |   |   SMTP Server  |   |   AI Service    |
| (vendors/users) |   |  (caching & session)   |   |  (email sender) |   | (optional AI)   |
+----------------+   +-------------------------+   +----------------+   +----------------+
```

### Component Diagram

```
Client --> API Gateway --> Backend Controller --> Service Layer --> Repository
                                                     |                  |
                                                     v                  v
                                                 Scheduler           PostgreSQL
                                                     |
                                                     v
                                                 Email Service
```

## Prerequisites

Before setup, ensure the following are installed:

- **Java 17+** (JDK 17 or newer)
- **Maven 3.8+** (or use the included `./mvnw` wrapper)
- **Docker** (20.10+)
- **Docker Compose** (1.29+ or built-in compose)
- **Git**
- **Node.js 16+** (for frontend development)

Optional:
- **Postman** for API testing
- **IDE**: IntelliJ IDEA, VS Code, or Eclipse

## Setup

### 1. Clone the repository

```bash
git clone https://github.com/Apoorva668/vendor-risk-assessment.git
cd vendor-risk-assessment-repo
```

### 2. Copy example environment file

Windows PowerShell:
```powershell
copy .env.example .env
```

macOS / Linux:
```bash
cp .env.example .env
```

### 3. Update `.env`

Open `.env` and set values for your local environment.

### 4. Start services with Docker Compose

```bash
docker-compose up --build
```

This starts the complete stack:
- `postgres` on `localhost:5432`
- `redis` on `localhost:6379`
- `backend` on `localhost:8080`
- `ai-service` on `localhost:8000`
- `frontend` on `localhost:3000`

### 5. Access the application

- Backend API: `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- Frontend (if available): `http://localhost:3000`

## .env.example Reference Table

| Variable | Example | Description |
|---|---|---|
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://postgres:5432/vendor_db` | JDBC connection URL for PostgreSQL |
| `SPRING_DATASOURCE_USERNAME` | `postgres` | PostgreSQL username |
| `SPRING_DATASOURCE_PASSWORD` | `postgres123` | PostgreSQL password |
| `JWT_SECRET` | `change-me-in-production-minimum-32-chars` | Secret key for signing JWT tokens |
| `JWT_EXPIRATION` | `3600000` | JWT expiration in milliseconds (1 hour) |
| `MAIL_SMTP_HOST` | `smtp.gmail.com` | SMTP server hostname |
| `MAIL_SMTP_PORT` | `587` | SMTP server port |
| `MAIL_SMTP_USERNAME` | `your-email@gmail.com` | SMTP username/email account |
| `MAIL_SMTP_PASSWORD` | `your-app-password` | SMTP password or app-specific password |
| `MAIL_FROM` | `noreply@vendor-assessment.com` | Default sender email address |
| `SPRING_REDIS_HOST` | `redis` | Redis hostname |
| `SPRING_REDIS_PORT` | `6379` | Redis port |
| `SPRING_JPA_HIBERNATE_DDL_AUTO` | `update` | Hibernate DDL auto setting |
| `SPRING_PROFILES_ACTIVE` | `docker` | Active Spring profile |

> **Important**: Never commit `.env` to source control.

## Project Structure

```
vendor-risk-assessment-repo/
├── backend/
│   ├── src/main/java/com/internship/tool/
│   │   ├── config/             # Spring configuration classes
│   │   ├── controller/         # REST controllers
│   │   ├── dto/                # Request/response DTOs
│   │   ├── entity/             # JPA entity models
│   │   ├── exception/          # Custom exceptions
│   │   ├── filter/             # JWT filter and request filters
│   │   ├── repository/         # Spring Data JPA repositories
│   │   ├── scheduler/          # Scheduled actions and jobs
│   │   ├── service/            # Business logic services
│   │   └── util/               # Utility classes
│   ├── src/main/resources/     # Application resources and Flyway migrations
│   ├── src/test/java/          # Unit and integration tests
│   ├── pom.xml                 # Maven configuration
│   └── Dockerfile              # Backend Docker image
├── frontend/                   # Frontend scaffolding and Nginx config
├── ai-service/                 # Python AI support service
├── docker-compose.yml          # Local orchestration
├── .env.example                # Environment variable template
├── README.md                   # Project documentation
└── CODE_REVIEW_DAY15.md        # Code review report
```

## Running the Backend Locally

From the `backend/` directory:

```bash
cd backend
./mvnw clean package
./mvnw spring-boot:run
```

Default local URL:
- `http://localhost:8080`
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`

## API Documentation

The backend exposes Swagger UI at:

```bash
http://localhost:8080/swagger-ui/index.html
```

Example endpoints:
- `POST /auth/login`
- `POST /auth/register`
- `GET /api/vendors/{id}/risk`
- `POST /api/vendors/create`
- `GET /api/vendors/risk/report`
- `POST /files/upload`
- `GET /files/{id}`

## Troubleshooting

### Docker Troubleshooting

```bash
docker-compose up --build
docker-compose ps
docker-compose logs backend
docker-compose down -v
```

### Common Fixes
- Ensure `.env` exists and contains correct values
- Verify `JWT_SECRET` is set and strong
- Confirm PostgreSQL and Redis are reachable
- Use `docker-compose logs backend` for backend errors

## Contributing

1. Create a feature branch:
   ```bash
git checkout -b feature/your-feature
```
2. Make changes and add tests
3. Commit with a meaningful message
4. Push branch and open a PR

## License

This project is part of an internship program and is maintained for educational purposes.

---

**Last Updated**: May 8, 2026
