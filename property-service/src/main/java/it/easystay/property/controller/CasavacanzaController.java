package it.easystay.property.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import it.easystay.property.dto.CasavacanzaDTO;
import it.easystay.property.dto.CasavacanzaResponseDTO;
import it.easystay.property.service.CasavacanzaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/casevacanza")
@RequiredArgsConstructor
@Tag(name = "Casavacanza", description = "API per la gestione delle case vacanza")
public class CasavacanzaController {
    
    private final CasavacanzaService casavacanzaService;
    
    @GetMapping
    @Operation(summary = "Ottieni tutte le case vacanza")
    public ResponseEntity<List<CasavacanzaResponseDTO>> getAllCasevacanza() {
        return ResponseEntity.ok(casavacanzaService.getAll());
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Ottieni una casa vacanza per ID")
    public ResponseEntity<CasavacanzaResponseDTO> getCasavacanzaById(@PathVariable Long id) {
        return ResponseEntity.ok(casavacanzaService.getById(id));
    }
    
    @GetMapping("/citta/{citta}")
    @Operation(summary = "Cerca case vacanza per città")
    public ResponseEntity<List<CasavacanzaResponseDTO>> getCasevacanzaByCitta(@PathVariable String citta) {
        return ResponseEntity.ok(casavacanzaService.findByCitta(citta));
    }
    
    @PostMapping
    @Operation(summary = "Crea una nuova casa vacanza")
    public ResponseEntity<CasavacanzaResponseDTO> createCasavacanza(@Valid @RequestBody CasavacanzaDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(casavacanzaService.create(dto));
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Aggiorna una casa vacanza esistente")
    public ResponseEntity<CasavacanzaResponseDTO> updateCasavacanza(
            @PathVariable Long id,
            @Valid @RequestBody CasavacanzaDTO dto) {
        return ResponseEntity.ok(casavacanzaService.update(id, dto));
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Elimina una casa vacanza")
    public ResponseEntity<Void> deleteCasavacanza(@PathVariable Long id) {
        casavacanzaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
