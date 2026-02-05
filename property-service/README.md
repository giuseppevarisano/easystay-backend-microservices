# Property Service

Microservizio per la gestione delle case vacanza (casevacanza) nell'architettura EasyStay.

## Descrizione

Il Property Service gestisce tutte le operazioni CRUD relative alle case vacanza, includendo:
- Creazione, lettura, aggiornamento ed eliminazione di proprietà
- Ricerca per città
- Validazione dei dati in ingresso
- Integrazione con Eureka per service discovery

## Tecnologie

- **Java 17**
- **Spring Boot 3.4.1**
- **Spring Data JPA** - Persistenza dati
- **Spring Cloud Netflix Eureka Client** - Service discovery
- **H2 Database** - Database in-memory per sviluppo
- **MapStruct** - Mapping tra entity e DTO
- **Lombok** - Riduzione boilerplate
- **SpringDoc OpenAPI** - Documentazione API

## Struttura

```
property-service/
├── src/
│   ├── main/
│   │   ├── java/it/easystay/property/
│   │   │   ├── PropertyServiceApplication.java    # Main application
│   │   │   ├── controller/
│   │   │   │   └── CasavacanzaController.java    # REST controller
│   │   │   ├── dto/
│   │   │   │   ├── CasavacanzaDTO.java           # Request DTO
│   │   │   │   └── CasavacanzaResponseDTO.java   # Response DTO
│   │   │   ├── mapper/
│   │   │   │   └── CasavacanzaMapper.java        # MapStruct mapper
│   │   │   ├── model/
│   │   │   │   └── Casavacanza.java              # JPA entity
│   │   │   ├── repository/
│   │   │   │   └── CasavacanzaRepository.java    # JPA repository
│   │   │   └── service/
│   │   │       └── CasavacanzaService.java       # Business logic
│   │   └── resources/
│   │       ├── application.properties            # Configurazione
│   │       └── data.sql                          # Dati di test
│   └── test/
│       └── java/it/easystay/property/
│           └── PropertyServiceApplicationTests.java
└── pom.xml
```

## Configurazione

### application.properties

```properties
spring.application.name=property-service
server.port=8082

# Eureka Client
eureka.client.service-url.defaultZone=http://localhost:8761/eureka/

# H2 Database
spring.datasource.url=jdbc:h2:mem:propertydb
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.defer-datasource-initialization=true

# H2 Console
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

## Endpoints REST

Tutti gli endpoint sono disponibili su `http://localhost:8082/api/casevacanza`

### GET /api/casevacanza
Ottieni tutte le case vacanza

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "nome": "Villa Marina",
    "indirizzo": "Via del Mare 123",
    "prezzoNotte": 150.0,
    "citta": "Napoli",
    "version": 0
  }
]
```

### GET /api/casevacanza/{id}
Ottieni una casa vacanza per ID

**Response:** `200 OK` o `404 Not Found`

### GET /api/casevacanza/citta/{citta}
Cerca case vacanza per città

**Response:** `200 OK`

### POST /api/casevacanza
Crea una nuova casa vacanza

**Request Body:**
```json
{
  "nome": "Villa Marina",
  "indirizzo": "Via del Mare 123",
  "prezzoNotte": 150.0,
  "citta": "Napoli"
}
```

**Response:** `201 Created`

### PUT /api/casevacanza/{id}
Aggiorna una casa vacanza esistente

**Request Body:** Come POST

**Response:** `200 OK` o `404 Not Found`

### DELETE /api/casevacanza/{id}
Elimina una casa vacanza

**Response:** `204 No Content` o `404 Not Found`

## Modello Dati

### Casavacanza Entity

| Campo | Tipo | Descrizione |
|-------|------|-------------|
| id | Long | Identificatore univoco (auto-generato) |
| nome | String | Nome della casa vacanza (obbligatorio) |
| indirizzo | String | Indirizzo completo (obbligatorio) |
| prezzoNotte | Double | Prezzo per notte in euro (obbligatorio, positivo) |
| citta | String | Città di ubicazione (obbligatorio) |
| version | Long | Versione per ottimistic locking |

## Avvio del Servizio

### Prerequisiti
- Java 17
- Maven 3.6+
- Eureka Server in esecuzione su porta 8761

### Compilazione
```bash
mvn clean install
```

### Esecuzione
```bash
mvn spring-boot:run
```

Oppure:
```bash
java -jar target/property-service-0.0.1-SNAPSHOT.jar
```

## Testing

```bash
mvn test
```

## Accesso alla Console H2

Una volta avviato il servizio, accedi alla console H2:

**URL:** http://localhost:8082/h2-console

**Configurazione:**
- JDBC URL: `jdbc:h2:mem:propertydb`
- Username: `sa`
- Password: _(vuota)_

## Documentazione API

La documentazione interattiva Swagger è disponibile su:

http://localhost:8082/swagger-ui/index.html

## Integrazione

Il servizio si registra automaticamente con Eureka Server e può essere invocato tramite:
- Accesso diretto: `http://localhost:8082`
- Tramite API Gateway: `http://localhost:8080/property-service`
- Service discovery: `http://PROPERTY-SERVICE/api/casevacanza`

## Exception Handling

Il servizio utilizza `ResourceNotFoundException` dal modulo `common` per gestire le risorse non trovate.

## Note di Sviluppo

- Il database H2 viene popolato automaticamente con dati di esempio all'avvio
- La configurazione `spring.jpa.defer-datasource-initialization=true` assicura che lo schema venga creato prima di eseguire `data.sql`
- MapStruct genera automaticamente le implementazioni dei mapper durante la compilazione
- Optimistic locking abilitato tramite il campo `@Version`
