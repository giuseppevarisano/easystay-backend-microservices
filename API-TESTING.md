# API Testing Guide - EasyStay Microservices

Questa guida fornisce esempi pratici per testare tutte le API del sistema EasyStay.

## Prerequisiti

- Servizi avviati (localmente, Docker, o Kubernetes)
- Tool per API testing (curl, Postman, o simili)
- API Gateway disponibile su http://localhost:8080

## Variabili di Ambiente

Per semplificare i test, definisci queste variabili:

```bash
export API_BASE=http://localhost:8080
export TOKEN=""  # Verrà popolato dopo il login
```

## 1. Autenticazione (Auth Service)

### 1.1 Registrazione Nuovo Utente

```bash
curl -X POST $API_BASE/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "mario.rossi@example.com",
    "password": "SecurePass123!",
    "nome": "Mario Rossi",
    "ruolo": "USER"
  }'
```

**Risposta Attesa:**
```json
{
  "email": "mario.rossi@example.com",
  "nome": "Mario Rossi",
  "ruolo": "USER",
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Salva il token:**
```bash
export TOKEN="eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
```

### 1.2 Login Utente Esistente

```bash
curl -X POST $API_BASE/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "mario.rossi@example.com",
    "password": "SecurePass123!"
  }'
```

**Risposta Attesa:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

### 1.3 Registrazione Admin

```bash
curl -X POST $API_BASE/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@easystay.it",
    "password": "AdminPass123!",
    "nome": "Admin User",
    "ruolo": "ADMIN"
  }'
```

## 2. Gestione Case Vacanza (Property Service)

### 2.1 Lista Tutte le Case

```bash
curl -X GET $API_BASE/api/casevacanza
```

**Risposta Attesa:**
```json
[
  {
    "id": 1,
    "nome": "Villa sul Mare",
    "indirizzo": "Via Caracciolo 123",
    "citta": "Napoli",
    "prezzoNotte": 150.00,
    "version": 0
  },
  ...
]
```

### 2.2 Dettaglio Casa Specifica

```bash
curl -X GET $API_BASE/api/casevacanza/1
```

### 2.3 Ricerca per Città

```bash
curl -X GET $API_BASE/api/casevacanza/citta/Roma
```

**Risposta Attesa:**
```json
[
  {
    "id": 3,
    "nome": "Appartamento Centro Storico",
    "indirizzo": "Via del Corso 45",
    "citta": "Roma",
    "prezzoNotte": 120.00,
    "version": 0
  }
]
```

### 2.4 Crea Nuova Casa

```bash
curl -X POST $API_BASE/api/casevacanza \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Casa di Montagna",
    "indirizzo": "Via Alpina 10",
    "citta": "Cortina",
    "prezzoNotte": 200.00
  }'
```

**Risposta Attesa:**
```json
{
  "id": 6,
  "nome": "Casa di Montagna",
  "indirizzo": "Via Alpina 10",
  "citta": "Cortina",
  "prezzoNotte": 200.00,
  "version": 0
}
```

### 2.5 Aggiorna Casa Esistente

```bash
curl -X PUT $API_BASE/api/casevacanza/6 \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Chalet di Montagna",
    "indirizzo": "Via Alpina 10",
    "citta": "Cortina",
    "prezzoNotte": 250.00
  }'
```

### 2.6 Elimina Casa

```bash
curl -X DELETE $API_BASE/api/casevacanza/6
```

## 3. Gestione Prenotazioni (Booking Service)

### 3.1 Lista Tutte le Prenotazioni

```bash
curl -X GET $API_BASE/api/prenotazioni
```

### 3.2 Dettaglio Prenotazione Specifica

```bash
curl -X GET $API_BASE/api/prenotazioni/1
```

### 3.3 Prenotazioni per Utente

```bash
curl -X GET $API_BASE/api/prenotazioni/utente/1
```

### 3.4 Prenotazioni per Casa

```bash
curl -X GET $API_BASE/api/prenotazioni/casa/1
```

### 3.5 Crea Nuova Prenotazione

```bash
curl -X POST $API_BASE/api/prenotazioni \
  -H "Content-Type: application/json" \
  -d '{
    "dataInizio": "2024-08-01",
    "dataFine": "2024-08-07",
    "casaId": 1,
    "utenteId": 1
  }'
```

**Risposta Attesa:**
```json
{
  "id": 1,
  "dataInizio": "2024-08-01",
  "dataFine": "2024-08-07",
  "casaId": 1,
  "utenteId": 1
}
```

### 3.6 Aggiorna Prenotazione

```bash
curl -X PUT $API_BASE/api/prenotazioni/1 \
  -H "Content-Type: application/json" \
  -d '{
    "dataInizio": "2024-08-01",
    "dataFine": "2024-08-10",
    "casaId": 1,
    "utenteId": 1
  }'
```

### 3.7 Elimina Prenotazione

```bash
curl -X DELETE $API_BASE/api/prenotazioni/1
```

## 4. Test di Validazione

### 4.1 Test Validazione Email

```bash
# Email invalida
curl -X POST $API_BASE/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "email-non-valida",
    "password": "Pass123",
    "nome": "Test",
    "ruolo": "USER"
  }'
```

**Risposta Attesa:** 400 Bad Request

### 4.2 Test Prezzo Negativo

```bash
curl -X POST $API_BASE/api/casevacanza \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Test Casa",
    "indirizzo": "Via Test",
    "citta": "Test",
    "prezzoNotte": -50.00
  }'
```

**Risposta Attesa:** 400 Bad Request

### 4.3 Test Date Invalide

```bash
# Data fine prima di data inizio
curl -X POST $API_BASE/api/prenotazioni \
  -H "Content-Type: application/json" \
  -d '{
    "dataInizio": "2024-08-10",
    "dataFine": "2024-08-05",
    "casaId": 1,
    "utenteId": 1
  }'
```

**Risposta Attesa:** 400 Bad Request con messaggio "La data di fine deve essere successiva alla data di inizio"

### 4.4 Test Prenotazione Sovrapposta

```bash
# Prima prenotazione
curl -X POST $API_BASE/api/prenotazioni \
  -H "Content-Type: application/json" \
  -d '{
    "dataInizio": "2024-08-01",
    "dataFine": "2024-08-07",
    "casaId": 1,
    "utenteId": 1
  }'

# Prenotazione sovrapposta (deve fallire)
curl -X POST $API_BASE/api/prenotazioni \
  -H "Content-Type: application/json" \
  -d '{
    "dataInizio": "2024-08-05",
    "dataFine": "2024-08-10",
    "casaId": 1,
    "utenteId": 2
  }'
```

**Risposta Attesa:** 400 Bad Request con messaggio sulle date sovrapposte

### 4.5 Test Casa Non Esistente

```bash
curl -X POST $API_BASE/api/prenotazioni \
  -H "Content-Type: application/json" \
  -d '{
    "dataInizio": "2024-08-01",
    "dataFine": "2024-08-07",
    "casaId": 999,
    "utenteId": 1
  }'
```

**Risposta Attesa:** 404 Not Found o errore Feign

## 5. Test di Performance

### 5.1 Test Load Balancing

Crea più istanze di un servizio e verifica il bilanciamento:

```bash
# Scala property-service
docker-compose up --scale property-service=3 -d

# Fai più richieste e osserva i logs
for i in {1..10}; do
  curl -X GET $API_BASE/api/casevacanza
  sleep 1
done

# Verifica nei logs quale istanza ha gestito le richieste
docker-compose logs property-service
```

### 5.2 Test Stress

```bash
# Installa Apache Bench
sudo apt-get install apache2-utils

# Test con 1000 richieste, 10 concorrenti
ab -n 1000 -c 10 http://localhost:8080/api/casevacanza
```

## 6. Test Eureka e Service Discovery

### 6.1 Verifica Servizi Registrati

```bash
curl http://localhost:8761/eureka/apps | jq
```

### 6.2 Dettaglio Servizio Specifico

```bash
curl http://localhost:8761/eureka/apps/property-service | jq
```

### 6.3 Health Check Servizi

```bash
# Eureka
curl http://localhost:8761/actuator/health

# API Gateway
curl http://localhost:8080/actuator/health

# Auth Service
curl http://localhost:8081/actuator/health

# Property Service
curl http://localhost:8082/actuator/health

# Booking Service
curl http://localhost:8083/actuator/health
```

## 7. Postman Collection

Puoi importare questa collection in Postman:

```json
{
  "info": {
    "name": "EasyStay Microservices",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "variable": [
    {
      "key": "baseUrl",
      "value": "http://localhost:8080"
    },
    {
      "key": "token",
      "value": ""
    }
  ],
  "item": [
    {
      "name": "Auth",
      "item": [
        {
          "name": "Register",
          "request": {
            "method": "POST",
            "header": [],
            "body": {
              "mode": "raw",
              "raw": "{\n  \"email\": \"test@example.com\",\n  \"password\": \"Pass123\",\n  \"nome\": \"Test User\",\n  \"ruolo\": \"USER\"\n}",
              "options": {
                "raw": {
                  "language": "json"
                }
              }
            },
            "url": {
              "raw": "{{baseUrl}}/api/auth/register",
              "host": ["{{baseUrl}}"],
              "path": ["api", "auth", "register"]
            }
          }
        },
        {
          "name": "Login",
          "request": {
            "method": "POST",
            "header": [],
            "body": {
              "mode": "raw",
              "raw": "{\n  \"email\": \"test@example.com\",\n  \"password\": \"Pass123\"\n}",
              "options": {
                "raw": {
                  "language": "json"
                }
              }
            },
            "url": {
              "raw": "{{baseUrl}}/api/auth/login",
              "host": ["{{baseUrl}}"],
              "path": ["api", "auth", "login"]
            }
          }
        }
      ]
    }
  ]
}
```

## 8. Script di Test Completo

Salva questo come `test-all.sh`:

```bash
#!/bin/bash

API_BASE="http://localhost:8080"

echo "=== Test EasyStay Microservices ==="
echo ""

# 1. Health Checks
echo "1. Health Checks..."
curl -s http://localhost:8761/actuator/health | jq -r '.status'
curl -s http://localhost:8080/actuator/health | jq -r '.status'
curl -s http://localhost:8081/actuator/health | jq -r '.status'
curl -s http://localhost:8082/actuator/health | jq -r '.status'
curl -s http://localhost:8083/actuator/health | jq -r '.status'
echo ""

# 2. Registrazione
echo "2. Registrazione nuovo utente..."
REGISTER_RESPONSE=$(curl -s -X POST $API_BASE/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test'$(date +%s)'@example.com",
    "password": "Pass123",
    "nome": "Test User",
    "ruolo": "USER"
  }')
echo $REGISTER_RESPONSE | jq
TOKEN=$(echo $REGISTER_RESPONSE | jq -r '.token')
echo "Token: $TOKEN"
echo ""

# 3. Lista case
echo "3. Lista case vacanza..."
curl -s $API_BASE/api/casevacanza | jq
echo ""

# 4. Crea prenotazione
echo "4. Crea prenotazione..."
curl -s -X POST $API_BASE/api/prenotazioni \
  -H "Content-Type: application/json" \
  -d '{
    "dataInizio": "2024-08-01",
    "dataFine": "2024-08-07",
    "casaId": 1,
    "utenteId": 1
  }' | jq
echo ""

echo "=== Test completati ==="
```

Rendi eseguibile e lancia:
```bash
chmod +x test-all.sh
./test-all.sh
```

## 9. Troubleshooting API

### Problema: 503 Service Unavailable

**Causa**: Servizio non registrato su Eureka o non disponibile

**Soluzione**:
1. Verifica su Eureka dashboard: http://localhost:8761
2. Controlla health: `curl http://localhost:808X/actuator/health`
3. Verifica logs: `docker-compose logs <service-name>`

### Problema: 401 Unauthorized

**Causa**: Token JWT mancante o non valido

**Soluzione**:
1. Effettua nuovamente login
2. Verifica che il token sia nel formato corretto
3. Controlla l'expiration del token

### Problema: 400 Bad Request

**Causa**: Dati di input non validi

**Soluzione**:
1. Verifica il formato JSON
2. Controlla le validazioni (@NotBlank, @NotNull, etc.)
3. Leggi il messaggio di errore nella risposta

### Problema: Timeout / Nessuna risposta

**Causa**: Servizio down o sovraccarico

**Soluzione**:
1. Controlla che tutti i servizi siano avviati
2. Verifica risorse sistema (RAM, CPU)
3. Controlla logs per errori

## 10. Best Practices per Testing

1. **Usa sempre HTTPS in produzione**
2. **Non hardcodare credenziali** nei test
3. **Usa variabili d'ambiente** per configurazione
4. **Implementa retry logic** per chiamate inter-servizio
5. **Testa scenari negativi** (errori, timeout, etc.)
6. **Monitora performance** durante i test
7. **Pulisci dati di test** dopo ogni suite
8. **Documenta casi edge** scoperti

---

Per ulteriori informazioni, consulta la documentazione completa nel [README.md](README.md).
