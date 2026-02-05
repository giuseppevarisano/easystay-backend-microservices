#!/bin/bash

# Build script per EasyStay Microservices

echo "=========================================="
echo "EasyStay Microservices - Build Script"
echo "=========================================="
echo ""

# Colori per output
GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Build del progetto
echo -e "${YELLOW}Building all microservices...${NC}"
mvn clean package -DskipTests

if [ $? -eq 0 ]; then
    echo -e "${GREEN}✓ Build completato con successo!${NC}"
    echo ""
    echo "JARs creati:"
    echo "  - eureka-server/target/eureka-server-0.0.1-SNAPSHOT.jar"
    echo "  - api-gateway/target/api-gateway-0.0.1-SNAPSHOT.jar"
    echo "  - auth-service/target/auth-service-0.0.1-SNAPSHOT.jar"
    echo "  - property-service/target/property-service-0.0.1-SNAPSHOT.jar"
    echo "  - booking-service/target/booking-service-0.0.1-SNAPSHOT.jar"
    echo ""
    echo -e "${YELLOW}Per avviare i servizi con Docker:${NC}"
    echo "  docker-compose up -d"
    echo ""
    echo -e "${YELLOW}Per avviare i servizi su Kubernetes:${NC}"
    echo "  kubectl apply -f k8s/"
else
    echo -e "${RED}✗ Build fallito!${NC}"
    exit 1
fi
