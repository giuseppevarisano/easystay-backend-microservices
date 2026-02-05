package it.easystay.booking.controller;

import it.easystay.booking.dto.PrenotazioneDTO;
import it.easystay.booking.dto.PrenotazioneResponseDTO;
import it.easystay.booking.service.PrenotazioneService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prenotazioni")
@RequiredArgsConstructor
@Slf4j
public class PrenotazioneController {
    
    private final PrenotazioneService prenotazioneService;
    
    @GetMapping
    public ResponseEntity<List<PrenotazioneResponseDTO>> getAllBookings() {
        log.info("GET /api/prenotazioni - Get all bookings");
        List<PrenotazioneResponseDTO> bookings = prenotazioneService.getAll();
        return ResponseEntity.ok(bookings);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PrenotazioneResponseDTO> getBookingById(@PathVariable Long id) {
        log.info("GET /api/prenotazioni/{} - Get booking by ID", id);
        PrenotazioneResponseDTO booking = prenotazioneService.getById(id);
        return ResponseEntity.ok(booking);
    }
    
    @GetMapping("/utente/{utenteId}")
    public ResponseEntity<List<PrenotazioneResponseDTO>> getBookingsByUserId(@PathVariable Long utenteId) {
        log.info("GET /api/prenotazioni/utente/{} - Get bookings by user ID", utenteId);
        List<PrenotazioneResponseDTO> bookings = prenotazioneService.findByUtenteId(utenteId);
        return ResponseEntity.ok(bookings);
    }
    
    @GetMapping("/casa/{casaId}")
    public ResponseEntity<List<PrenotazioneResponseDTO>> getBookingsByPropertyId(@PathVariable Long casaId) {
        log.info("GET /api/prenotazioni/casa/{} - Get bookings by property ID", casaId);
        List<PrenotazioneResponseDTO> bookings = prenotazioneService.findByCasaId(casaId);
        return ResponseEntity.ok(bookings);
    }
    
    @PostMapping
    public ResponseEntity<PrenotazioneResponseDTO> createBooking(@Valid @RequestBody PrenotazioneDTO dto) {
        log.info("POST /api/prenotazioni - Create new booking");
        PrenotazioneResponseDTO created = prenotazioneService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<PrenotazioneResponseDTO> updateBooking(
            @PathVariable Long id,
            @Valid @RequestBody PrenotazioneDTO dto) {
        log.info("PUT /api/prenotazioni/{} - Update booking", id);
        PrenotazioneResponseDTO updated = prenotazioneService.update(id, dto);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBooking(@PathVariable Long id) {
        log.info("DELETE /api/prenotazioni/{} - Delete booking", id);
        prenotazioneService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
