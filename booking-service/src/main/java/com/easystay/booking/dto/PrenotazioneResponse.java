package com.easystay.booking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Risposta prenotazione")
public class PrenotazioneResponse {

    @Schema(description = "ID della prenotazione", example = "1")
    private Long id;

    @Schema(description = "ID dell'utente", example = "1")
    private Long utenteId;

    @Schema(description = "ID della casa vacanza", example = "1")
    private Long casaVacanzaId;

    @Schema(description = "Data inizio", example = "2026-06-01")
    private LocalDate dataInizio;

    @Schema(description = "Data fine", example = "2026-06-07")
    private LocalDate dataFine;

    @Schema(description = "Numero di ospiti", example = "4")
    private Integer numeroOspiti;

    @Schema(description = "Prezzo totale", example = "900.00")
    private BigDecimal prezzoTotale;

    @Schema(description = "Stato della prenotazione", example = "CONFERMATA")
    private String stato;

    @Schema(description = "Data di creazione")
    private LocalDateTime createdAt;
}
