package com.easystay.house.controller;

import com.easystay.house.dto.CasaVacanzaRequest;
import com.easystay.house.dto.CasaVacanzaResponse;
import com.easystay.house.service.HouseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/case-vacanza")
@RequiredArgsConstructor
@Tag(name = "Case Vacanza", description = "API per gestione case vacanza")
@SecurityRequirement(name = "Bearer Authentication")
public class HouseController {

    private final HouseService houseService;

    @Operation(
            summary = "Crea una nuova Casa vacanza",
            description = "Creazione nuova casa vacanza possibile solo se sei loggato come utente con ruolo ADMIN."
    )
    @PostMapping(value = "/crea", consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CasaVacanzaResponse> creaCasaVacanza(
            @Valid @RequestBody CasaVacanzaRequest request,
            HttpServletRequest httpRequest) {

        Long proprietarioId = (Long) httpRequest.getAttribute("userId");
        CasaVacanzaResponse response = houseService.creaCasaVacanza(request, proprietarioId);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
            summary = "Cerca Case vacanza disponibili per una determinata città",
            description = "Ricerca case vacanze disponibili."
    )
    @GetMapping("/disponibili")
    public ResponseEntity<List<CasaVacanzaResponse>> trovaCaseDisponibili(
            @RequestParam String citta) {

        List<CasaVacanzaResponse> case_ = houseService.trovaCaseDisponibiliPerCitta(citta);
        return ResponseEntity.ok(case_);
    }

    @Operation(
            summary = "Trova casa vacanza per ID",
            description = "Recupera i dettagli di una casa vacanza specifica"
    )
    @GetMapping("/{id}")
    public ResponseEntity<CasaVacanzaResponse> trovaCasaById(@PathVariable Long id) {
        try {
            CasaVacanzaResponse response = houseService.trovaCasaById(id);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @Operation(
            summary = "Trova case del proprietario",
            description = "Recupera tutte le case di un proprietario"
    )
    @GetMapping("/proprietario")
    public ResponseEntity<List<CasaVacanzaResponse>> trovaCaseProprietario(HttpServletRequest httpRequest) {
        Long proprietarioId = (Long) httpRequest.getAttribute("userId");
        List<CasaVacanzaResponse> case_ = houseService.trovaCasePerProprietario(proprietarioId);
        return ResponseEntity.ok(case_);
    }
}
