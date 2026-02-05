# Auth Service

Authentication and User Management Service for EasyStay microservices architecture.

## Overview

The Auth Service handles user authentication and authorization using JWT (JSON Web Tokens). It provides secure user registration and login functionality with role-based access control.

## Features

- User registration with validation
- User authentication (login)
- JWT token generation and validation
- Role-based authorization (USER, ADMIN)
- Password encryption using BCrypt
- Spring Security integration
- H2 in-memory database (for development)

## Technology Stack

- Java 17
- Spring Boot 3.4.1
- Spring Security
- Spring Data JPA
- JWT (io.jsonwebtoken)
- Lombok
- H2 Database
- Maven

## API Endpoints

### Authentication Endpoints

All authentication endpoints are publicly accessible (no authentication required).

#### Register User
```
POST /api/auth/register
Content-Type: application/json

Request Body:
{
  "email": "user@example.com",
  "password": "securePassword123",
  "nome": "John Doe",
  "ruolo": "USER"
}

Response (201 Created):
{
  "email": "user@example.com",
  "nome": "John Doe",
  "ruolo": "USER",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

#### Login
```
POST /api/auth/login
Content-Type: application/json

Request Body:
{
  "email": "user@example.com",
  "password": "securePassword123"
}

Response (200 OK):
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

## Configuration

### Environment Variables

For production, always configure these environment variables:

- `JWT_SECRET`: Secret key for JWT token signing (required in production)
- `JWT_EXPIRATION`: Token expiration time in milliseconds (default: 86400000 = 24 hours)
- `SPRING_DATASOURCE_URL`: Database URL (default: H2 in-memory)
- `SPRING_DATASOURCE_USERNAME`: Database username
- `SPRING_DATASOURCE_PASSWORD`: Database password

### application.properties

```properties
# Server Configuration
server.port=8081

# Eureka Configuration
eureka.client.serviceUrl.defaultZone=http://localhost:8761/eureka/

# Database Configuration (H2)
spring.datasource.url=jdbc:h2:mem:authdb
spring.datasource.username=sa
spring.datasource.password=

# JWT Configuration
# WARNING: Default values are for DEVELOPMENT ONLY
jwt.secret=${JWT_SECRET:404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970}
jwt.expiration=${JWT_EXPIRATION:86400000}
```

## Security

### JWT Authentication

The service uses JWT tokens for stateless authentication. After successful login or registration, clients receive a JWT token that must be included in the Authorization header for subsequent requests to protected endpoints.

```
Authorization: Bearer <token>
```

### CSRF Protection

CSRF protection is disabled for this service as it implements a stateless REST API using JWT authentication. JWT tokens are sent in the Authorization header (not cookies), making them immune to CSRF attacks.

### Password Encryption

All passwords are encrypted using BCrypt before storage.

### Role-Based Access Control

The service supports two roles:
- `USER`: Standard user role
- `ADMIN`: Administrator role with elevated privileges

## Building and Running

### Prerequisites

- JDK 17 or higher
- Maven 3.6+

### Build

```bash
mvn clean package
```

### Run

```bash
mvn spring-boot:run
```

Or run the JAR:

```bash
java -jar target/auth-service-0.0.1-SNAPSHOT.jar
```

### Run with Environment Variables

```bash
JWT_SECRET=your-secret-key \
JWT_EXPIRATION=3600000 \
java -jar target/auth-service-0.0.1-SNAPSHOT.jar
```

## Database

### H2 Console

The H2 console is enabled for development:
- URL: http://localhost:8081/h2-console
- JDBC URL: jdbc:h2:mem:authdb
- Username: sa
- Password: (empty)

## Testing with cURL

### Register a new user

```bash
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123",
    "nome": "Test User",
    "ruolo": "USER"
  }'
```

### Login

```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }'
```

## API Documentation

Once the service is running, Swagger UI is available at:
- http://localhost:8081/swagger-ui.html

OpenAPI specification:
- http://localhost:8081/v3/api-docs

## Architecture

### Package Structure

```
it.easystay.auth
├── config          # Security and application configuration
├── controller      # REST API controllers
├── dto             # Data Transfer Objects
├── model           # JPA entities
├── repository      # Data access layer
├── security        # JWT and security components
└── service         # Business logic
```

### Key Components

- **Utente**: User entity implementing UserDetails
- **UtenteRepository**: JPA repository for user data access
- **AuthenticationService**: Business logic for authentication
- **JwtService**: JWT token generation and validation
- **JwtAuthenticationFilter**: Filter for JWT validation on requests
- **SecurityConfig**: Spring Security configuration
- **AuthController**: REST API endpoints

## Integration with Other Services

This service is part of the EasyStay microservices architecture and:
- Registers with Eureka Service Discovery (port 8761)
- Is accessible through API Gateway (port 8080)

## Future Enhancements

- Account status management (enabled/disabled, locked, expired)
- Email verification
- Password reset functionality
- Refresh token support
- OAuth2 integration
- Multi-factor authentication (MFA)

## License

Part of the EasyStay microservices project.
