# Spring Advanced

Sample application demonstrating advanced Spring Boot features for the lecture "Advanced Spring Boot. Schedulers. Cache. AOP. Logging".

## Features

- **Schedulers**: Payment processing with ShedLock for distributed task coordination
- **Caching**: EhCache integration with Spring Cache abstraction
- **AOP**: Custom `@Monitored` annotation for execution time logging
- **Logging**: Structured logging with SLF4j/Lombok

## Tech Stack

- Spring Boot 3.5.8
- Java 21
- PostgreSQL 16.2
- Liquibase (migrations)
- JPA/Hibernate
- Maven

## Prerequisites

- Java 21+
- Docker and Docker Compose
- Maven 3.6+

## Running the Application

1. Start PostgreSQL:
```bash
docker-compose -f docker-compose-infra.yml up -d
```

2. Run the application:
```bash
mvn spring-boot:run
```

The application will start on `http://localhost:9090`

## Database

- **User**: scott
- **Password**: tiger
- **Database**: finance
- **Port**: 5432

See [Database Schema](docs/database-schema.md) for detailed ER diagram and table descriptions.

## Project Structure

- **Controllers**: REST endpoints for contracts and payments
- **Services**: Business logic with caching and monitoring
- **Schedulers**: Automated payment processing
- **AOP**: Method execution monitoring via `ServiceMonitor` aspect
- **Repository**: JPA data access layer
