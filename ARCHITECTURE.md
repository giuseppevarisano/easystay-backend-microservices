# Architettura EasyStay Microservices

## Diagramma dell'Architettura

```
                                    ┌─────────────────┐
                                    │   Client App    │
                                    │  (Frontend/     │
                                    │   Mobile)       │
                                    └────────┬────────┘
                                             │
                                             │ HTTP/REST
                                             ▼
                                    ┌─────────────────┐
                                    │   API Gateway   │
                                    │   Port: 8080    │
                                    │  (Load Balance) │
                                    └────────┬────────┘
                                             │
                        ┌────────────────────┼────────────────────┐
                        │                    │                    │
                        ▼                    ▼                    ▼
              ┌──────────────────┐ ┌──────────────────┐ ┌──────────────────┐
              │  Auth Service    │ │ Property Service │ │ Booking Service  │
              │   Port: 8081     │ │   Port: 8082     │ │   Port: 8083     │
              │                  │ │                  │ │                  │
              │ - JWT Auth       │ │ - CRUD Houses    │ │ - CRUD Bookings  │
              │ - User Mgmt      │ │ - Search Cities  │ │ - Validation     │
              └────────┬─────────┘ └────────┬─────────┘ └────────┬─────────┘
                       │                    │                    │
                       │                    │         Feign      │
                       │                    │      ◄─────────────┤
                       │                    │                    │
                       ▼                    ▼                    ▼
              ┌──────────────────┐ ┌──────────────────┐ ┌──────────────────┐
              │   H2 Database    │ │   H2 Database    │ │   H2 Database    │
              │   (authdb)       │ │  (propertydb)    │ │  (bookingdb)     │
              └──────────────────┘ └──────────────────┘ └──────────────────┘
                       │                    │                    │
                       └────────────────────┼────────────────────┘
                                            │
                                            │ Service Registration
                                            │ & Discovery
                                            ▼
                                   ┌──────────────────┐
                                   │  Eureka Server   │
                                   │   Port: 8761     │
                                   │ (Service         │
                                   │  Discovery)      │
                                   └──────────────────┘
```

## Componenti dell'Architettura

### 1. API Gateway (Spring Cloud Gateway)
**Responsabilità:**
- Entry point unico per tutti i client
- Routing delle richieste ai microservizi appropriati
- Load balancing lato client
- CORS configuration
- Potenziale punto per rate limiting e caching

**Routing:**
```
/api/auth/**         → Auth Service (8081)
/api/casevacanza/**  → Property Service (8082)
/api/prenotazioni/** → Booking Service (8083)
```

**Vantaggi:**
- Semplifica l'accesso per i client
- Centralizza cross-cutting concerns
- Nasconde la complessità dei microservizi

### 2. Eureka Server (Netflix OSS)
**Responsabilità:**
- Service Registry: registrazione di tutti i microservizi
- Service Discovery: permette ai servizi di trovarsi tra loro
- Health monitoring dei servizi registrati

**Funzionamento:**
1. Ogni microservizio si registra all'avvio
2. Invia heartbeat periodici
3. Eureka mantiene un registro aggiornato
4. I client possono interrogare Eureka per trovare istanze

**Dashboard:**
- Visualizzazione grafica di tutti i servizi registrati
- Stato di salute delle istanze
- Informazioni sulle repliche

### 3. Auth Service
**Responsabilità:**
- Autenticazione utenti (login)
- Registrazione nuovi utenti
- Generazione JWT tokens
- Validazione JWT tokens

**Database Schema:**
```sql
CREATE TABLE utenti (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    nome VARCHAR(255),
    ruolo VARCHAR(20)
);
```

**API Endpoints:**
- POST `/api/auth/register` - Registrazione
- POST `/api/auth/login` - Autenticazione

**Sicurezza:**
- Password hashing con BCrypt
- JWT con firma HMAC-SHA256
- Token expiration configurabile

### 4. Property Service
**Responsabilità:**
- Gestione case vacanza (CRUD)
- Ricerca per città
- Validazione dati proprietà

**Database Schema:**
```sql
CREATE TABLE casevacanza (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(255) NOT NULL,
    indirizzo VARCHAR(255) NOT NULL,
    citta VARCHAR(255) NOT NULL,
    prezzo_notte DECIMAL(10,2) NOT NULL,
    version INT
);
```

**API Endpoints:**
- GET `/api/casevacanza` - Lista tutte
- GET `/api/casevacanza/{id}` - Dettaglio
- GET `/api/casevacanza/citta/{citta}` - Per città
- POST `/api/casevacanza` - Creazione
- PUT `/api/casevacanza/{id}` - Aggiornamento
- DELETE `/api/casevacanza/{id}` - Eliminazione

**Features:**
- Optimistic locking con @Version
- Validazione input con Bean Validation
- MapStruct per mapping DTO-Entity

### 5. Booking Service
**Responsabilità:**
- Gestione prenotazioni (CRUD)
- Validazione date e overlapping
- Comunicazione con Property Service
- Query per utente e per casa

**Database Schema:**
```sql
CREATE TABLE prenotazioni (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    data_inizio DATE NOT NULL,
    data_fine DATE NOT NULL,
    casa_id BIGINT NOT NULL,
    utente_id BIGINT NOT NULL
);
```

**API Endpoints:**
- GET `/api/prenotazioni` - Lista tutte
- GET `/api/prenotazioni/{id}` - Dettaglio
- GET `/api/prenotazioni/utente/{id}` - Per utente
- GET `/api/prenotazioni/casa/{id}` - Per casa
- POST `/api/prenotazioni` - Creazione
- PUT `/api/prenotazioni/{id}` - Aggiornamento
- DELETE `/api/prenotazioni/{id}` - Eliminazione

**Business Logic:**
- Validazione date (start < end, no past dates)
- Check sovrapposizioni prenotazioni
- Verifica esistenza proprietà (via Feign)

**Inter-service Communication:**
- Usa Feign Client per chiamare Property Service
- Service discovery automatico via Eureka
- Client-side load balancing

### 6. Common Module
**Contenuto:**
- DTOs condivisi
- Eccezioni custom (ResourceNotFoundException, BadRequestException)
- Model comuni (Ruolo enum)
- Utility classes

**Benefici:**
- Elimina duplicazione codice
- Facilita manutenzione
- Tipizzazione forte per comunicazione inter-servizio

## Pattern e Pratiche

### 1. Database per Microservizio
Ogni servizio ha il proprio database H2:
- **Auth Service**: authdb
- **Property Service**: propertydb
- **Booking Service**: bookingdb

**Vantaggi:**
- Isolamento completo dei dati
- Scaling indipendente
- Failure isolation

**Svantaggi:**
- Impossibilità di JOIN tra servizi
- Gestione transazioni distribuite complessa
- Duplicazione possibile di alcuni dati

### 2. Comunicazione Sincrona (REST)
**Booking Service → Property Service:**
- Usa OpenFeign per chiamate REST
- Service discovery via Eureka
- Retry automatico in caso di errore

**Alternative:**
- Eventi asincroni con Message Queue (RabbitMQ, Kafka)
- GraphQL Federation
- gRPC per performance

### 3. API Gateway Pattern
- Single entry point
- Request routing
- Cross-cutting concerns (auth, logging, rate limiting)

### 4. Service Registry Pattern
- Dynamic service discovery
- Load balancing
- Health checking

### 5. Resilience Patterns (da implementare)
- **Circuit Breaker**: Previene cascate di fallimenti
- **Retry**: Riprova in caso di errori temporanei
- **Timeout**: Limita tempo di attesa
- **Bulkhead**: Isola risorse per servizio

## Flussi Principali

### Flusso di Autenticazione
```
1. Client → API Gateway → Auth Service: POST /api/auth/login
2. Auth Service: Valida credenziali
3. Auth Service: Genera JWT token
4. Auth Service → API Gateway → Client: JWT token
5. Client: Usa token in header Authorization per richieste successive
```

### Flusso di Prenotazione
```
1. Client → API Gateway → Booking Service: POST /api/prenotazioni
2. Booking Service: Valida date
3. Booking Service → Property Service (Feign): GET /api/casevacanza/{id}
4. Property Service → Booking Service: Dettagli casa
5. Booking Service: Valida disponibilità (no overlap)
6. Booking Service: Salva prenotazione
7. Booking Service → API Gateway → Client: Prenotazione creata
```

## Scalabilità

### Scaling Orizzontale
Ogni servizio può scalare indipendentemente:
```bash
# Kubernetes
kubectl scale deployment property-service --replicas=5

# Docker Compose
docker-compose up --scale property-service=5
```

### Load Balancing
- **Client-side**: Ribbon (integrato in Eureka client)
- **Server-side**: Kubernetes Services, NGINX

### Caching (da implementare)
- Redis per cache distribuita
- Cache a livello di API Gateway
- Cache locale in ogni servizio

## Monitoraggio e Observability

### Health Checks
Ogni servizio espone:
- `/actuator/health` - Stato del servizio
- `/actuator/info` - Informazioni applicazione

### Metriche (Actuator + Prometheus)
- `/actuator/metrics` - Metriche applicazione
- `/actuator/prometheus` - Formato Prometheus

### Logging
- Structured logging con Logback
- Correlation IDs per tracciare richieste
- Log aggregation (ELK stack) in produzione

### Distributed Tracing (da implementare)
- Spring Cloud Sleuth per correlation IDs
- Zipkin/Jaeger per visualizzazione trace

## Sicurezza

### Authentication & Authorization
- JWT tokens per autenticazione stateless
- Role-based access control (USER, ADMIN)
- Token validation in ogni servizio

### Network Security
- HTTPS in produzione
- mTLS tra servizi
- Network policies in Kubernetes

### Secrets Management
- Kubernetes Secrets per dati sensibili
- Environment variables per configurazione
- HashiCorp Vault in produzione

## Deployment Strategies

### Blue-Green Deployment
1. Deploy nuova versione (green)
2. Test su green environment
3. Switch traffic da blue a green
4. Mantieni blue come rollback

### Canary Deployment
1. Deploy nuova versione a sottoinsieme utenti
2. Monitor metriche
3. Gradualmente aumenta traffico
4. Rollback se problemi

### Rolling Update
1. Aggiorna pod uno alla volta
2. Health check prima di procedere
3. Zero-downtime deployment

## Evoluzione Futura

### Microservizi Aggiuntivi
- **Payment Service**: Gestione pagamenti
- **Notification Service**: Email/SMS notifiche
- **Review Service**: Recensioni e rating
- **Analytics Service**: Reporting e statistiche

### Miglioramenti Tecnologici
- Message Queue (RabbitMQ/Kafka) per eventi asincroni
- API composition con GraphQL
- Service Mesh (Istio) per comunicazioni avanzate
- Event Sourcing e CQRS per audit trail
- Distributed cache con Redis
- Persistent databases (PostgreSQL, MongoDB)

### DevOps
- CI/CD con GitHub Actions
- Infrastructure as Code (Terraform)
- Automated testing (contract testing)
- Performance testing (JMeter, Gatling)
- Security scanning (OWASP, Snyk)

---

## Confronto: Monolite vs Microservizi

| Aspetto | Monolite | Microservizi |
|---------|----------|--------------|
| **Deployment** | Singolo artefatto | Multipli artefatti indipendenti |
| **Scaling** | Verticale (scale-up) | Orizzontale (scale-out) per servizio |
| **Database** | Condiviso | Uno per servizio |
| **Technology Stack** | Omogeneo | Eterogeneo possibile |
| **Team Organization** | Team funzionali | Team per servizio |
| **Testing** | Più semplice | Più complesso (integration) |
| **Fault Isolation** | No | Sì |
| **Deployment Speed** | Lento | Veloce (per servizio) |
| **Initial Complexity** | Bassa | Alta |
| **Operational Overhead** | Basso | Alto |
| **Best For** | Startup, MVP | Produzione, scaling |

---

Per informazioni su deployment, vedere [DEPLOYMENT.md](DEPLOYMENT.md).
