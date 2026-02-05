.PHONY: help build up down logs clean test

help: ## Mostra questo messaggio di aiuto
	@echo "Comandi disponibili per EasyStay Microservices:"
	@echo ""
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "  \033[36m%-15s\033[0m %s\n", $$1, $$2}'

build: ## Build delle immagini Docker
	docker-compose build

up: ## Avvia tutti i servizi
	docker-compose up -d
	@echo ""
	@echo "✅ Servizi avviati!"
	@echo "API Gateway: http://localhost:8080"
	@echo "Auth Service: http://localhost:8081"
	@echo "House Service: http://localhost:8082"
	@echo "Booking Service: http://localhost:8083"
	@echo ""
	@echo "Swagger UI:"
	@echo "  - Auth: http://localhost:8081/swagger-ui.html"
	@echo "  - House: http://localhost:8082/swagger-ui.html"
	@echo "  - Booking: http://localhost:8083/swagger-ui.html"

down: ## Ferma tutti i servizi
	docker-compose down

restart: down up ## Riavvia tutti i servizi

logs: ## Visualizza i log di tutti i servizi
	docker-compose logs -f

logs-auth: ## Visualizza i log dell'auth-service
	docker-compose logs -f auth-service

logs-house: ## Visualizza i log dell'house-service
	docker-compose logs -f house-service

logs-booking: ## Visualizza i log del booking-service
	docker-compose logs -f booking-service

logs-gateway: ## Visualizza i log dell'api-gateway
	docker-compose logs -f api-gateway

ps: ## Mostra lo stato dei container
	docker-compose ps

clean: ## Ferma e rimuove tutti i container e volumi
	docker-compose down -v
	@echo "✅ Container e volumi rimossi"

test: ## Esegue lo script di test delle API
	@echo "Assicurati che i servizi siano avviati..."
	@sleep 2
	./test-api.sh

rebuild: clean build up ## Rebuild completo

dev-auth: ## Avvia auth-service in modalità sviluppo
	cd auth-service && mvn spring-boot:run

dev-house: ## Avvia house-service in modalità sviluppo
	cd house-service && mvn spring-boot:run

dev-booking: ## Avvia booking-service in modalità sviluppo
	cd booking-service && mvn spring-boot:run

dev-gateway: ## Avvia api-gateway in modalità sviluppo
	cd api-gateway && mvn spring-boot:run
