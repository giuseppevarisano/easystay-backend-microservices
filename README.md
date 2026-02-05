# EasyStay Backend - Microservices Architecture

Backend EasyStay in architettura a microservizi con Spring Boot, Spring Cloud Gateway, Eureka, Docker e Kubernetes.

## 📋 Descrizione

Questo progetto è una reimplementazione del backend monolitico [EasyStay-Backend](https://github.com/giuseppevarisano/EasyStay-Backend) utilizzando un'architettura a microservizi. L'applicazione gestisce prenotazioni di case vacanza con autenticazione JWT e servizi scalabili indipendenti.

## 🏗️ Architettura

### Microservizi

1. **Eureka Server** (porta 8761)
   - Service Discovery e Service Registry
   - Consente la scoperta automatica dei servizi
   
2. **API Gateway** (porta 8080)
   - Gateway unificato per tutti i servizi
   - Routing intelligente basato su path
   - Load balancing integrato
   
3. **Auth Service** (porta 8081)
   - Gestione autenticazione utenti
   - Generazione e validazione JWT token
   - Registrazione nuovi utenti
   
4. **Property Service** (porta 8082)
   - Gestione case vacanza (CRUD)
   - Ricerca per città
   - Database H2 dedicato
   
5. **Booking Service** (porta 8083)
   - Gestione prenotazioni (CRUD)
   - Validazione date e overlap
   - Comunicazione con Property Service via Feign Client

### Common Module
Modulo condiviso contenente:
- DTOs comuni
- Eccezioni personalizzate
- Enumerazioni (Ruolo)
- Utility classes

## 🛠️ Tecnologie

- **Java 17**
- **Spring Boot 3.4.1**
- **Spring Cloud 2024.0.0**
- **Spring Cloud Gateway** - API Gateway
- **Netflix Eureka** - Service Discovery
- **OpenFeign** - Inter-service communication
- **Spring Security** - Sicurezza e JWT
- **Spring Data JPA** - Persistenza dati
- **H2 Database** - Database in-memory
- **MapStruct** - Object mapping
- **Lombok** - Riduzione boilerplate
- **OpenAPI/Swagger** - Documentazione API
- **Docker** - Containerizzazione
- **Kubernetes** - Orchestrazione

## 🚀 Quick Start

### Prerequisiti
- Java 17+
- Maven 3.6+
- Docker (opzionale)
- Kubernetes/Minikube (opzionale)

### Build del progetto

```bash
# Clone del repository
git clone https://github.com/giuseppevarisano/easystay-backend-microservices.git
cd easystay-backend-microservices

# Build di tutti i moduli
mvn clean install
```

### Esecuzione in locale

1. **Avviare Eureka Server**
```bash
cd eureka-server
mvn spring-boot:run
```
Accedi a: http://localhost:8761

2. **Avviare API Gateway**
```bash
cd api-gateway
mvn spring-boot:run
```

3. **Avviare Auth Service**
```bash
cd auth-service
mvn spring-boot:run
```

4. **Avviare Property Service**
```bash
cd property-service
mvn spring-boot:run
```

5. **Avviare Booking Service**
```bash
cd booking-service
mvn spring-boot:run
```

### Esecuzione con Docker Compose

```bash
# Build delle immagini Docker
mvn clean package
docker-compose build

# Avvio di tutti i servizi
docker-compose up -d

# Verifica stato
docker-compose ps

# Logs
docker-compose logs -f

# Stop
docker-compose down
```

### Deployment su Kubernetes

```bash
# Crea namespace
kubectl apply -f k8s/namespace.yaml

# Deploy servizi
kubectl apply -f k8s/eureka-server.yaml
kubectl apply -f k8s/api-gateway.yaml
kubectl apply -f k8s/auth-service.yaml
kubectl apply -f k8s/property-service.yaml
kubectl apply -f k8s/booking-service.yaml

# Verifica deployment
kubectl get pods -n easystay
kubectl get services -n easystay

# Accedi all'API Gateway
kubectl port-forward -n easystay service/api-gateway 8080:8080
```

## 📚 API Documentation

Ogni servizio espone la propria documentazione Swagger:

- **Auth Service**: http://localhost:8081/swagger-ui.html
- **Property Service**: http://localhost:8082/swagger-ui.html
- **Booking Service**: http://localhost:8083/swagger-ui.html

### Endpoint Principali (via API Gateway)

**Autenticazione**
```bash
# Registrazione
POST http://localhost:8080/api/auth/register
Content-Type: application/json
{
  "email": "user@example.com",
  "password": "password123",
  "nome": "Mario Rossi",
  "ruolo": "USER"
}

# Login
POST http://localhost:8080/api/auth/login
Content-Type: application/json
{
  "email": "user@example.com",
  "password": "password123"
}
```

**Case Vacanza**
```bash
# Lista tutte
GET http://localhost:8080/api/casevacanza

# Per città
GET http://localhost:8080/api/casevacanza/citta/Roma

# Crea nuova
POST http://localhost:8080/api/casevacanza
Content-Type: application/json
{
  "nome": "Villa sul mare",
  "indirizzo": "Via Mare 123",
  "citta": "Napoli",
  "prezzoNotte": 150.0
}
```

**Prenotazioni**
```bash
# Lista tutte
GET http://localhost:8080/api/prenotazioni

# Crea prenotazione
POST http://localhost:8080/api/prenotazioni
Content-Type: application/json
{
  "dataInizio": "2024-07-01",
  "dataFine": "2024-07-07",
  "casaId": 1,
  "utenteId": 1
}
```

## 📁 Struttura del Progetto

```
easystay-backend-microservices/
├── common/                      # Modulo condiviso
│   └── src/main/java/it/easystay/common/
│       ├── dto/                 # DTOs comuni
│       ├── exception/           # Eccezioni custom
│       └── model/               # Model comuni (Ruolo)
├── eureka-server/              # Service Discovery
├── api-gateway/                # API Gateway
├── auth-service/               # Servizio autenticazione
│   └── src/main/java/it/easystay/auth/
│       ├── config/             # Configurazioni
│       ├── controller/         # REST Controllers
│       ├── dto/                # Data Transfer Objects
│       ├── model/              # Entity (Utente)
│       ├── repository/         # JPA Repositories
│       ├── security/           # JWT & Security
│       └── service/            # Business Logic
├── property-service/           # Servizio case vacanza
│   └── src/main/java/it/easystay/property/
│       ├── controller/
│       ├── dto/
│       ├── mapper/
│       ├── model/              # Entity (Casavacanza)
│       ├── repository/
│       └── service/
├── booking-service/            # Servizio prenotazioni
│   └── src/main/java/it/easystay/booking/
│       ├── client/             # Feign Clients
│       ├── controller/
│       ├── dto/
│       ├── mapper/
│       ├── model/              # Entity (Prenotazione)
│       ├── repository/
│       └── service/
├── k8s/                        # Kubernetes manifests
│   ├── namespace.yaml
│   ├── eureka-server.yaml
│   ├── api-gateway.yaml
│   ├── auth-service.yaml
│   ├── property-service.yaml
│   └── booking-service.yaml
├── docker-compose.yml          # Docker Compose config
└── pom.xml                     # Parent POM
```

## 🔒 Sicurezza

- **JWT Authentication**: Token-based authentication per tutti i servizi
- **Password Encryption**: BCrypt per l'hashing delle password
- **CORS Configuration**: Configurabile per ambiente
- **API Gateway Security**: Routing sicuro e load balancing

## 🧪 Testing

```bash
# Test su tutti i moduli
mvn test

# Test su un modulo specifico
cd auth-service
mvn test

# Test con coverage
mvn clean test jacoco:report
```

## 📊 Monitoraggio

Ogni servizio espone endpoint Actuator per il monitoraggio:
- `/actuator/health` - Health check
- `/actuator/info` - Informazioni applicazione
- `/actuator/metrics` - Metriche

## 🔄 Comunicazione tra Servizi

- **Service Discovery**: Eureka per la registrazione e scoperta automatica
- **Load Balancing**: Client-side load balancing con Ribbon
- **API Gateway**: Routing centralizzato con Spring Cloud Gateway
- **Feign Client**: Comunicazione sincrona REST tra servizi

## 🌟 Caratteristiche Microservizi

✅ **Indipendenza**: Ogni servizio è autonomo con il proprio database  
✅ **Scalabilità**: Ogni servizio può scalare indipendentemente  
✅ **Resilienza**: Failure isolation tra servizi  
✅ **Deployment**: Deploy indipendente per ogni servizio  
✅ **Tecnologia**: Possibilità di usare tecnologie diverse per servizio  
✅ **Team**: Team diversi possono lavorare su servizi diversi  

## 📝 Differenze dal Monolite

| Aspetto | Monolite | Microservizi |
|---------|----------|--------------|
| Database | Condiviso | Uno per servizio |
| Deployment | Singolo | Multiplo |
| Scalabilità | Verticale | Orizzontale |
| Complessità | Bassa | Alta |
| Fault Isolation | No | Sì |
| Technology Stack | Unico | Multiplo possibile |

## 🤝 Contribuire

1. Fork del progetto
2. Crea un branch per la feature (`git checkout -b feature/AmazingFeature`)
3. Commit delle modifiche (`git commit -m 'Add some AmazingFeature'`)
4. Push al branch (`git push origin feature/AmazingFeature`)
5. Apri una Pull Request

## 📄 Licenza

Questo progetto è sotto licenza MIT.

## 👤 Autore

**Giuseppe Varisano**

- GitHub: [@giuseppevarisano](https://github.com/giuseppevarisano)

## 🙏 Acknowledgments

- Basato sul progetto monolitico [EasyStay-Backend](https://github.com/giuseppevarisano/EasyStay-Backend)
- Spring Cloud per l'ecosistema microservizi
- Netflix OSS per Eureka
