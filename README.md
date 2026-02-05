# EasyStay Backend - Architettura a Microservizi

Backend completo per EasyStay implementato con un'architettura a microservizi utilizzando Spring Boot, PostgreSQL, JWT per l'autenticazione e Docker Compose per l'orchestrazione.

## 🏗️ Architettura

Il sistema è composto da 4 microservizi principali:

### 1. **Auth Service** (porta 8081)
- Registrazione utenti con ruolo USER o ADMIN
- Autenticazione e generazione JWT token
- Gestione credenziali utente

### 2. **House Service** (porta 8082)
- Creazione case vacanza (solo ADMIN)
- Ricerca case disponibili per città
- Gestione dettagli case vacanza
- Protezione JWT su tutte le rotte

### 3. **Booking Service** (porta 8083)
- Creazione prenotazioni con verifica disponibilità
- Recupero prenotazioni per utente con paginazione
- Calcolo automatico prezzo totale
- Protezione JWT su tutte le rotte

### 4. **API Gateway** (porta 8080)
- Routing centralizzato verso i microservizi
- Validazione JWT per tutte le rotte (eccetto `/api/auth/**`)
- Gestione CORS
- Punto di ingresso unico per i client

### Database
Ogni microservizio ha il proprio database PostgreSQL:
- `auth-db` (porta 5432)
- `house-db` (porta 5433)
- `booking-db` (porta 5434)

## 🚀 Avvio del Sistema

### Prerequisiti
- Docker e Docker Compose installati
- Porte 8080-8083 e 5432-5434 disponibili

### Avvio con Docker Compose

```bash
# Avvia tutti i servizi
docker-compose up -d

# Verifica lo stato dei container
docker-compose ps

# Visualizza i log
docker-compose logs -f

# Ferma tutti i servizi
docker-compose down

# Ferma e rimuovi anche i volumi dei database
docker-compose down -v
```

### Avvio in locale (sviluppo)

Ogni microservizio può essere avviato indipendentemente:

```bash
# Auth Service
cd auth-service
mvn spring-boot:run

# House Service
cd house-service
mvn spring-boot:run

# Booking Service
cd booking-service
mvn spring-boot:run

# API Gateway
cd api-gateway
mvn spring-boot:run
```

## 📖 API Endpoints

Tutte le richieste passano attraverso l'**API Gateway** sulla porta **8080**.

### Autenticazione (nessun JWT richiesto)

#### Registrazione
```http
POST http://localhost:8080/api/auth/register
Content-Type: application/json

{
  "email": "admin@easystay.com",
  "password": "AdminPassword123!",
  "nome": "Admin User",
  "ruolo": "ADMIN"
}
```

Risposta:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "userId": 1,
  "email": "admin@easystay.com",
  "nome": "Admin User",
  "ruolo": "ADMIN"
}
```

#### Login
```http
POST http://localhost:8080/api/auth/login
Content-Type: application/json

{
  "email": "admin@easystay.com",
  "password": "AdminPassword123!"
}
```

### Case Vacanza (JWT richiesto)

#### Crea Casa Vacanza (solo ADMIN)
```http
POST http://localhost:8080/api/case-vacanza/crea
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json

{
  "nome": "Villa al Mare",
  "descrizione": "Bellissima villa con vista mare",
  "citta": "Roma",
  "indirizzo": "Via Roma 123",
  "prezzoPerNotte": 150.00,
  "numeroStanze": 3,
  "numeroPostiLetto": 6
}
```

#### Cerca Case Disponibili per Città
```http
GET http://localhost:8080/api/case-vacanza/disponibili?citta=Roma
Authorization: Bearer <JWT_TOKEN>
```

#### Dettagli Casa Vacanza
```http
GET http://localhost:8080/api/case-vacanza/1
Authorization: Bearer <JWT_TOKEN>
```

### Prenotazioni (JWT richiesto)

#### Crea Prenotazione
```http
POST http://localhost:8080/api/prenotazioni/crea
Authorization: Bearer <JWT_TOKEN>
Content-Type: application/json

{
  "casaVacanzaId": 1,
  "dataInizio": "2026-06-01",
  "dataFine": "2026-06-07",
  "numeroOspiti": 4
}
```

#### Recupera Prenotazioni Utente
```http
GET http://localhost:8080/api/prenotazioni/utente/1?page=0&size=10&sort=createdAt,desc
Authorization: Bearer <JWT_TOKEN>
```

## 🔐 Autenticazione JWT

Tutte le rotte tranne `/api/auth/**` richiedono un JWT token valido nell'header:

```
Authorization: Bearer <JWT_TOKEN>
```

Il token contiene:
- `userId`: ID dell'utente
- `email`: Email dell'utente
- `ruolo`: Ruolo dell'utente (USER o ADMIN)

## 📊 Swagger/OpenAPI

Ogni microservizio espone la documentazione Swagger:

- Auth Service: http://localhost:8081/swagger-ui.html
- House Service: http://localhost:8082/swagger-ui.html
- Booking Service: http://localhost:8083/swagger-ui.html

## 🛠️ Tecnologie Utilizzate

- **Spring Boot 3.2.2** - Framework principale
- **Spring Cloud Gateway** - API Gateway
- **Spring Data JPA** - ORM
- **PostgreSQL 15** - Database
- **JWT (jjwt 0.12.3)** - Autenticazione
- **SpringDoc OpenAPI** - Documentazione API
- **Lombok** - Riduzione boilerplate
- **Docker & Docker Compose** - Containerizzazione

## 📁 Struttura del Progetto

```
easystay-backend-microservices/
├── auth-service/
│   ├── src/main/java/com/easystay/auth/
│   │   ├── model/          # Entity (Utente)
│   │   ├── repository/     # JPA Repository
│   │   ├── dto/            # Request/Response DTOs
│   │   ├── service/        # Business Logic
│   │   ├── controller/     # REST Controllers
│   │   ├── security/       # JWT Util
│   │   └── config/         # Security Configuration
│   ├── src/main/resources/
│   │   └── application.yml
│   ├── Dockerfile
│   └── pom.xml
│
├── house-service/
│   ├── src/main/java/com/easystay/house/
│   │   ├── model/          # Entity (CasaVacanza)
│   │   ├── repository/
│   │   ├── dto/
│   │   ├── service/
│   │   ├── controller/
│   │   ├── security/       # JWT Filter & Util
│   │   └── config/
│   ├── src/main/resources/
│   ├── Dockerfile
│   └── pom.xml
│
├── booking-service/
│   ├── src/main/java/com/easystay/booking/
│   │   ├── model/          # Entity (Prenotazione)
│   │   ├── repository/
│   │   ├── dto/
│   │   ├── service/
│   │   ├── controller/
│   │   ├── client/         # Inter-service communication
│   │   ├── security/
│   │   └── config/
│   ├── src/main/resources/
│   ├── Dockerfile
│   └── pom.xml
│
├── api-gateway/
│   ├── src/main/java/com/easystay/gateway/
│   │   ├── filter/         # JWT Authentication Filter
│   │   ├── security/       # JWT Util
│   │   └── config/         # Gateway Configuration
│   ├── src/main/resources/
│   ├── Dockerfile
│   └── pom.xml
│
├── docker-compose.yml
└── README.md
```

## 🔧 Configurazione

Le variabili d'ambiente sono configurate in `docker-compose.yml`:

- `JWT_SECRET`: Chiave segreta per firma JWT (stessa per tutti i servizi)
- `SPRING_DATASOURCE_*`: Configurazione database per ogni servizio
- `*_SERVICE_URL`: URL dei microservizi per comunicazione inter-service

## 📝 Note Importanti

1. **JWT Token**: Il token viene generato alla registrazione/login e deve essere incluso in tutte le richieste protette
2. **Ruoli**: Solo gli utenti con ruolo ADMIN possono creare case vacanza
3. **Validazione Date**: Il booking service verifica che le date non si sovrappongano con prenotazioni esistenti
4. **Paginazione**: Le prenotazioni supportano paginazione e ordinamento
5. **Comunicazione Inter-Service**: Il booking service comunica con house service via REST per validare le case vacanza

## 🧪 Test delle API

Esempio completo di workflow:

```bash
# 1. Registra un utente ADMIN
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@test.com","password":"Pass123!","nome":"Admin","ruolo":"ADMIN"}'

# 2. Salva il token dalla risposta
TOKEN="<token_dalla_risposta>"

# 3. Crea una casa vacanza
curl -X POST http://localhost:8080/api/case-vacanza/crea \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"nome":"Villa Roma","descrizione":"Bella villa","citta":"Roma","indirizzo":"Via Roma 1","prezzoPerNotte":100,"numeroStanze":2,"numeroPostiLetto":4}'

# 4. Cerca case disponibili
curl -X GET "http://localhost:8080/api/case-vacanza/disponibili?citta=Roma" \
  -H "Authorization: Bearer $TOKEN"

# 5. Registra un utente USER
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"user@test.com","password":"Pass123!","nome":"User","ruolo":"USER"}'

# 6. Login come USER e salva il nuovo token
USER_TOKEN="<token_dalla_risposta>"

# 7. Crea una prenotazione
curl -X POST http://localhost:8080/api/prenotazioni/crea \
  -H "Authorization: Bearer $USER_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"casaVacanzaId":1,"dataInizio":"2026-07-01","dataFine":"2026-07-07","numeroOspiti":2}'

# 8. Recupera le prenotazioni dell'utente
curl -X GET "http://localhost:8080/api/prenotazioni/utente/2?page=0&size=10" \
  -H "Authorization: Bearer $USER_TOKEN"
```

## 🐛 Debug

Per visualizzare i log di un servizio specifico:

```bash
# Tutti i servizi
docker-compose logs -f

# Solo un servizio
docker-compose logs -f auth-service
docker-compose logs -f house-service
docker-compose logs -f booking-service
docker-compose logs -f api-gateway
```

## 📄 Licenza

Questo progetto è sviluppato per scopi didattici.
