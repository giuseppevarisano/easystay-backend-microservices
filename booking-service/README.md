# Booking Service

The Booking Service is a microservice responsible for managing property reservations (prenotazioni) in the EasyStay platform.

## Features

- Create, read, update, and delete bookings
- Query bookings by user ID
- Query bookings by property ID
- Validate booking dates (start before end, no past dates)
- Check for overlapping bookings on the same property
- Validate property existence via Feign client communication with Property Service

## Technology Stack

- **Spring Boot 3.4.1**
- **Spring Cloud Netflix Eureka Client** - Service discovery
- **Spring Cloud OpenFeign** - Inter-service communication
- **Spring Data JPA** - Data persistence
- **H2 Database** - In-memory database
- **MapStruct** - DTO-Entity mapping
- **Lombok** - Reduce boilerplate code
- **Jakarta Validation** - Request validation

## Architecture

This service follows microservices architecture patterns:
- Stores only IDs (casaId, utenteId) instead of full entity references
- Uses Feign Client to validate property existence
- Registers with Eureka Server for service discovery
- Exposes RESTful endpoints for booking management

## Configuration

**Port:** 8083  
**Service Name:** booking-service  
**Database:** H2 (in-memory) at `jdbc:h2:mem:bookingdb`  
**Eureka Server:** http://localhost:8761/eureka/

See `application.properties` for full configuration.

## REST API Endpoints

### Get All Bookings
```
GET /api/prenotazioni
```

### Get Booking by ID
```
GET /api/prenotazioni/{id}
```

### Get Bookings by User ID
```
GET /api/prenotazioni/utente/{utenteId}
```

### Get Bookings by Property ID
```
GET /api/prenotazioni/casa/{casaId}
```

### Create Booking
```
POST /api/prenotazioni
Content-Type: application/json

{
  "dataInizio": "2024-06-01",
  "dataFine": "2024-06-07",
  "casaId": 1,
  "utenteId": 1
}
```

### Update Booking
```
PUT /api/prenotazioni/{id}
Content-Type: application/json

{
  "dataInizio": "2024-06-01",
  "dataFine": "2024-06-10",
  "casaId": 1,
  "utenteId": 1
}
```

### Delete Booking
```
DELETE /api/prenotazioni/{id}
```

## Business Rules

1. **Date Validation:** Start date must be before end date
2. **No Past Bookings:** Start date cannot be in the past
3. **Property Validation:** Property (casa) must exist in property-service
4. **No Overlapping:** Cannot book a property with overlapping dates

## Data Model

### Prenotazione Entity
- `id` (Long) - Primary key
- `dataInizio` (LocalDate) - Booking start date
- `dataFine` (LocalDate) - Booking end date
- `casaId` (Long) - Reference to property ID
- `utenteId` (Long) - Reference to user ID
- `version` (Long) - Optimistic locking

## Running the Service

### Prerequisites
1. Eureka Server running on port 8761
2. Property Service running on port 8082
3. Java 17 or higher
4. Maven 3.6 or higher

### Start the Service
```bash
mvn spring-boot:run
```

Or build and run the JAR:
```bash
mvn clean package
java -jar target/booking-service-0.0.1-SNAPSHOT.jar
```

### Access H2 Console
```
http://localhost:8083/h2-console
JDBC URL: jdbc:h2:mem:bookingdb
Username: sa
Password: (empty)
```

## Inter-Service Communication

The Booking Service communicates with:
- **Property Service** - Validates that properties exist before creating/updating bookings

## Error Handling

The service uses common exception handlers:
- `ResourceNotFoundException` - When booking or property not found (404)
- `BadRequestException` - For validation errors (400)
- Standard Spring validation for request bodies

## Future Enhancements

- Add booking status (pending, confirmed, cancelled)
- Integration with payment service
- Email notifications for bookings
- Booking cancellation policies
- Calendar availability check endpoint
