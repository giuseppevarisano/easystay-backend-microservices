# Guida al Deployment - EasyStay Microservices

Questa guida fornisce istruzioni dettagliate per il deployment dell'applicazione EasyStay in architettura microservizi.

## Indice

1. [Deployment Locale](#deployment-locale)
2. [Deployment con Docker Compose](#deployment-con-docker-compose)
3. [Deployment su Kubernetes](#deployment-su-kubernetes)
4. [Verifica e Testing](#verifica-e-testing)
5. [Troubleshooting](#troubleshooting)

---

## Deployment Locale

### Prerequisiti
- Java 17 o superiore
- Maven 3.6 o superiore
- 8GB RAM disponibili

### Passi per il deployment

1. **Build del progetto**
```bash
./build.sh
# oppure
mvn clean package -DskipTests
```

2. **Avvio dei servizi in ordine**

Terminal 1 - Eureka Server:
```bash
cd eureka-server
java -jar target/eureka-server-0.0.1-SNAPSHOT.jar
```
Attendi che Eureka sia disponibile su http://localhost:8761

Terminal 2 - API Gateway:
```bash
cd api-gateway
java -jar target/api-gateway-0.0.1-SNAPSHOT.jar
```

Terminal 3 - Auth Service:
```bash
cd auth-service
java -jar target/auth-service-0.0.1-SNAPSHOT.jar
```

Terminal 4 - Property Service:
```bash
cd property-service
java -jar target/property-service-0.0.1-SNAPSHOT.jar
```

Terminal 5 - Booking Service:
```bash
cd booking-service
java -jar target/booking-service-0.0.1-SNAPSHOT.jar
```

3. **Verifica**
- Eureka Dashboard: http://localhost:8761
- API Gateway: http://localhost:8080
- Swagger UI Auth: http://localhost:8081/swagger-ui.html
- Swagger UI Property: http://localhost:8082/swagger-ui.html
- Swagger UI Booking: http://localhost:8083/swagger-ui.html

---

## Deployment con Docker Compose

### Prerequisiti
- Docker 20.10 o superiore
- Docker Compose 2.0 o superiore
- 8GB RAM disponibili

### Passi per il deployment

1. **Build delle immagini**
```bash
# Build di tutti i moduli Maven
./build.sh

# Build delle immagini Docker
docker-compose build
```

2. **Avvio dei container**
```bash
# Avvio in background
docker-compose up -d

# Oppure in foreground (con logs)
docker-compose up
```

3. **Verifica stato**
```bash
# Lista container
docker-compose ps

# Logs di tutti i servizi
docker-compose logs -f

# Logs di un servizio specifico
docker-compose logs -f auth-service
```

4. **Accesso ai servizi**
- Eureka: http://localhost:8761
- API Gateway: http://localhost:8080
- Auth Service: http://localhost:8081
- Property Service: http://localhost:8082
- Booking Service: http://localhost:8083

5. **Stop dei servizi**
```bash
# Stop senza rimuovere container
docker-compose stop

# Stop e rimozione container
docker-compose down

# Stop, rimozione container e volumi
docker-compose down -v
```

### Configurazione Avanzata

**Variabili d'ambiente** (.env file):
```env
JWT_SECRET=your-secret-key-here
EUREKA_SERVER_URL=http://eureka-server:8761/eureka/
```

**Resource Limits** (docker-compose.yml):
```yaml
services:
  auth-service:
    deploy:
      resources:
        limits:
          cpus: '1'
          memory: 512M
```

---

## Deployment su Kubernetes

### Prerequisiti
- Kubernetes cluster (Minikube, Kind, o cloud provider)
- kubectl configurato
- 4GB RAM disponibili per il cluster

### Setup con Minikube

1. **Avvia Minikube**
```bash
minikube start --memory=4096 --cpus=2
```

2. **Abilita ingress (opzionale)**
```bash
minikube addons enable ingress
```

### Deployment dei servizi

1. **Build delle immagini Docker**

Per Minikube, usa il Docker daemon di Minikube:
```bash
eval $(minikube docker-env)
./build.sh

# Build immagini Docker
docker build -t easystay/eureka-server:latest ./eureka-server
docker build -t easystay/api-gateway:latest ./api-gateway
docker build -t easystay/auth-service:latest ./auth-service
docker build -t easystay/property-service:latest ./property-service
docker build -t easystay/booking-service:latest ./booking-service
```

2. **Deploy su Kubernetes**
```bash
# Crea namespace
kubectl apply -f k8s/namespace.yaml

# Deploy servizi in ordine
kubectl apply -f k8s/eureka-server.yaml
sleep 30  # Attendi che Eureka sia pronto

kubectl apply -f k8s/api-gateway.yaml
kubectl apply -f k8s/auth-service.yaml
kubectl apply -f k8s/property-service.yaml
kubectl apply -f k8s/booking-service.yaml
```

3. **Verifica deployment**
```bash
# Verifica pods
kubectl get pods -n easystay

# Verifica services
kubectl get services -n easystay

# Logs di un pod
kubectl logs -n easystay <pod-name>

# Descrizione pod
kubectl describe pod -n easystay <pod-name>
```

4. **Accesso ai servizi**

**Port Forwarding**:
```bash
# API Gateway
kubectl port-forward -n easystay service/api-gateway 8080:8080

# Eureka
kubectl port-forward -n easystay service/eureka-server 8761:8761
```

**Con Minikube**:
```bash
# Ottieni URL del servizio
minikube service api-gateway -n easystay --url
```

**Con LoadBalancer (cloud)**:
```bash
# Ottieni external IP
kubectl get service api-gateway -n easystay
```

### Scaling dei servizi

```bash
# Scale auth-service a 3 repliche
kubectl scale deployment auth-service -n easystay --replicas=3

# Scale property-service a 4 repliche
kubectl scale deployment property-service -n easystay --replicas=4

# Verifica scaling
kubectl get pods -n easystay
```

### Update dei servizi

```bash
# Build nuova immagine
docker build -t easystay/auth-service:v2 ./auth-service

# Update deployment
kubectl set image deployment/auth-service -n easystay \
  auth-service=easystay/auth-service:v2

# Rollback se necessario
kubectl rollout undo deployment/auth-service -n easystay

# Verifica rollout
kubectl rollout status deployment/auth-service -n easystay
```

### Cleanup

```bash
# Rimuovi tutti i servizi
kubectl delete -f k8s/

# Rimuovi namespace
kubectl delete namespace easystay

# Stop Minikube
minikube stop
```

---

## Verifica e Testing

### Health Checks

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

### Test API End-to-End

1. **Registrazione utente**
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123",
    "nome": "Test User",
    "ruolo": "USER"
  }'
```

2. **Login**
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }'
```

3. **Lista case vacanza**
```bash
curl http://localhost:8080/api/casevacanza
```

4. **Crea prenotazione**
```bash
curl -X POST http://localhost:8080/api/prenotazioni \
  -H "Content-Type: application/json" \
  -d '{
    "dataInizio": "2024-07-01",
    "dataFine": "2024-07-07",
    "casaId": 1,
    "utenteId": 1
  }'
```

---

## Troubleshooting

### Problema: Servizio non si registra su Eureka

**Soluzione**:
1. Verifica che Eureka sia avviato e disponibile
2. Controlla i logs del servizio: `docker-compose logs <service-name>`
3. Verifica la configurazione eureka.client.serviceUrl.defaultZone

### Problema: Container si riavvia continuamente

**Soluzione**:
1. Verifica i logs: `docker-compose logs <service-name>`
2. Verifica risorse disponibili: `docker stats`
3. Aumenta memory limit in docker-compose.yml se necessario

### Problema: Errore di connessione tra servizi

**Soluzione**:
1. Verifica che i servizi siano sulla stessa network
2. Usa il nome del servizio invece di localhost
3. Verifica Eureka dashboard per la registrazione

### Problema: Port già in uso

**Soluzione**:
```bash
# Trova processo che usa la porta
lsof -i :8080

# Termina processo
kill -9 <PID>

# Oppure cambia porta nel application.properties
```

### Problema: Pod in stato CrashLoopBackOff

**Soluzione**:
```bash
# Verifica logs
kubectl logs -n easystay <pod-name>

# Verifica eventi
kubectl describe pod -n easystay <pod-name>

# Verifica risorse
kubectl top pods -n easystay
```

### Debug Mode

Avvia servizi con debug abilitato:
```bash
java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:5005 \
  -jar target/auth-service-0.0.1-SNAPSHOT.jar
```

---

## Monitoraggio

### Metriche Prometheus

Ogni servizio espone metriche su `/actuator/prometheus`:
```bash
curl http://localhost:8081/actuator/prometheus
```

### Logs Aggregation

Visualizza logs di tutti i container:
```bash
docker-compose logs -f --tail=100
```

### Resource Usage

```bash
# Docker
docker stats

# Kubernetes
kubectl top pods -n easystay
kubectl top nodes
```

---

## Best Practices

1. **Sempre avviare Eureka per primo** e attendere che sia completamente avviato
2. **Configurare health checks** per ogni servizio
3. **Usare segreti Kubernetes** per dati sensibili (JWT_SECRET)
4. **Limitare risorse** nei deployment per evitare overconsumption
5. **Implementare readiness/liveness probes** in produzione
6. **Usare persistent volumes** per database in produzione
7. **Configurare backup automatici** dei dati
8. **Implementare logging centralizzato** (ELK stack)
9. **Usare service mesh** (Istio) per comunicazioni avanzate
10. **Implementare circuit breaker** (Resilience4j) per fault tolerance

---

## Risorse Utili

- [Spring Cloud Documentation](https://spring.io/projects/spring-cloud)
- [Docker Compose Documentation](https://docs.docker.com/compose/)
- [Kubernetes Documentation](https://kubernetes.io/docs/)
- [Netflix Eureka](https://github.com/Netflix/eureka)

---

Per ulteriori informazioni, consultare il [README principale](README.md) del progetto.
