package com.easystay.booking.controller;

import com.easystay.booking.dto.PrenotazioneRequest;
import com.easystay.booking.dto.PrenotazioneResponse;
import com.easystay.booking.dto.PrenotazioniCasaDettaglioResponse;
import com.easystay.booking.security.JwtUtil;
import com.easystay.booking.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/prenotazioni")
@RequiredArgsConstructor
@Tag(name = "Prenotazioni", description = "API per gestione prenotazioni")
@SecurityRequirement(name = "Bearer Authentication")
public class BookingController {

    private final BookingService bookingService;
    private final JwtUtil jwtUtil;

    @Operation(
            summary = "Crea una nuova prenotazione",
            description = "Verifica la disponibilità della casa e salva la prenotazione nel database."
    )
    @ApiResponse(responseCode = "201", description = "Prenotazione creata con successo")
    @ApiResponse(responseCode = "400", description = "Dati di input non validi o date incoerenti")
    @ApiResponse(responseCode = "404", description = "Casa vacanza non trovata")
    @PostMapping(value = "/crea", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> creaPrenotazione(
            @Valid @RequestBody PrenotazioneRequest request,
            HttpServletRequest httpRequest) {

        Long utenteId = (Long) httpRequest.getAttribute("userId");
        String authHeader = httpRequest.getHeader("Authorization");
        String token = authHeader.substring(7);

        try {
            PrenotazioneResponse response = bookingService.creaPrenotazione(request, utenteId, token);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @Operation(
            summary = "Recupera le prenotazioni di un utente",
            description = "Restituisce una pagina di prenotazioni filtrate per l'ID utente. Supporta la paginazione (page, size) e l'ordinamento (sort)."
    )
    @GetMapping("/utente/{utenteId}")
    public ResponseEntity<Page<PrenotazioneResponse>> trovaPrenotazioniUtente(
            @PathVariable Long utenteId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt,desc") String[] sort,
            HttpServletRequest httpRequest,
            @RequestHeader("Authorization") String authorizationHeader) {

        String token = authorizationHeader.startsWith("Bearer ")
                ? authorizationHeader.substring(7)
                : authorizationHeader;

        Object requestUserId = httpRequest.getAttribute("userId");
        Long authenticatedUserId;
        if (requestUserId instanceof Number number) {
            authenticatedUserId = number.longValue();
        } else {
            authenticatedUserId = jwtUtil.extractUserId(token);
        }
        
        // Solo l'utente stesso può vedere le proprie prenotazioni
        if (authenticatedUserId == null || !authenticatedUserId.equals(utenteId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        Sort.Direction direction = sort[1].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sort[0]));

        Page<PrenotazioneResponse> prenotazioni = bookingService.trovaPrenotazioniUtente(utenteId, pageable);
        return ResponseEntity.ok(prenotazioni);
    }

    @Operation(
            summary = "Recupera prenotazioni per casa vacanza (solo ADMIN)",
            description = "Restituisce dettagli casa, prenotazioni associate e dettagli utenti prenotanti."
    )
    @GetMapping("/{casaId}")
    public ResponseEntity<PrenotazioniCasaDettaglioResponse> trovaPrenotazioniPerCasa(
            @PathVariable Long casaId,
            @RequestHeader("Authorization") String authorizationHeader) {

        String token = authorizationHeader.startsWith("Bearer ")
                ? authorizationHeader.substring(7)
                : authorizationHeader;

        String ruolo = jwtUtil.extractRuolo(token);
        if (!"ADMIN".equalsIgnoreCase(ruolo)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        PrenotazioniCasaDettaglioResponse response = bookingService.trovaPrenotazioniPerCasa(casaId, token);
        return ResponseEntity.ok(response);
    }
}
