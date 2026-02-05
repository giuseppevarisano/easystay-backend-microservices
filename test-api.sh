#!/bin/bash

# Script per testare le API di EasyStay
# Assicurati che i servizi siano avviati con docker-compose up

BASE_URL="http://localhost:8080"

echo "========================================="
echo "   Test API EasyStay - Microservizi"
echo "========================================="
echo ""

# 1. Registra un utente ADMIN
echo "1. Registrazione utente ADMIN..."
ADMIN_RESPONSE=$(curl -s -X POST "$BASE_URL/api/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "admin@easystay.com",
    "password": "Admin123!",
    "nome": "Admin User",
    "ruolo": "ADMIN"
  }')

echo "Risposta: $ADMIN_RESPONSE"
ADMIN_TOKEN=$(echo $ADMIN_RESPONSE | grep -o '"token":"[^"]*' | cut -d'"' -f4)
ADMIN_ID=$(echo $ADMIN_RESPONSE | grep -o '"userId":[0-9]*' | cut -d':' -f2)
echo "Token ADMIN: $ADMIN_TOKEN"
echo "ID ADMIN: $ADMIN_ID"
echo ""

# 2. Crea una casa vacanza
echo "2. Creazione casa vacanza (ADMIN)..."
HOUSE_RESPONSE=$(curl -s -X POST "$BASE_URL/api/case-vacanza/crea" \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Villa al Mare",
    "descrizione": "Bellissima villa con vista mare",
    "citta": "Roma",
    "indirizzo": "Via Roma 123",
    "prezzoPerNotte": 150.00,
    "numeroStanze": 3,
    "numeroPostiLetto": 6
  }')

echo "Risposta: $HOUSE_RESPONSE"
HOUSE_ID=$(echo $HOUSE_RESPONSE | grep -o '"id":[0-9]*' | head -1 | cut -d':' -f2)
echo "ID Casa: $HOUSE_ID"
echo ""

# 3. Cerca case disponibili per città
echo "3. Ricerca case disponibili a Roma..."
SEARCH_RESPONSE=$(curl -s -X GET "$BASE_URL/api/case-vacanza/disponibili?citta=Roma" \
  -H "Authorization: Bearer $ADMIN_TOKEN")

echo "Risposta: $SEARCH_RESPONSE"
echo ""

# 4. Registra un utente USER
echo "4. Registrazione utente USER..."
USER_RESPONSE=$(curl -s -X POST "$BASE_URL/api/auth/register" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "user@easystay.com",
    "password": "User123!",
    "nome": "Mario Rossi",
    "ruolo": "USER"
  }')

echo "Risposta: $USER_RESPONSE"
USER_TOKEN=$(echo $USER_RESPONSE | grep -o '"token":"[^"]*' | cut -d'"' -f4)
USER_ID=$(echo $USER_RESPONSE | grep -o '"userId":[0-9]*' | cut -d':' -f2)
echo "Token USER: $USER_TOKEN"
echo "ID USER: $USER_ID"
echo ""

# 5. Crea una prenotazione
echo "5. Creazione prenotazione..."
BOOKING_RESPONSE=$(curl -s -X POST "$BASE_URL/api/prenotazioni/crea" \
  -H "Authorization: Bearer $USER_TOKEN" \
  -H "Content-Type: application/json" \
  -d "{
    \"casaVacanzaId\": $HOUSE_ID,
    \"dataInizio\": \"2026-07-01\",
    \"dataFine\": \"2026-07-07\",
    \"numeroOspiti\": 4
  }")

echo "Risposta: $BOOKING_RESPONSE"
echo ""

# 6. Recupera le prenotazioni dell'utente
echo "6. Recupero prenotazioni utente..."
BOOKINGS_RESPONSE=$(curl -s -X GET "$BASE_URL/api/prenotazioni/utente/$USER_ID?page=0&size=10&sort=createdAt,desc" \
  -H "Authorization: Bearer $USER_TOKEN")

echo "Risposta: $BOOKINGS_RESPONSE"
echo ""

echo "========================================="
echo "   Test completato!"
echo "========================================="
echo ""
echo "Riepilogo Token:"
echo "ADMIN_TOKEN=$ADMIN_TOKEN"
echo "USER_TOKEN=$USER_TOKEN"
echo ""
echo "Puoi usare questi token per testare altre API manualmente."
