package com.easystay.booking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "Richiesta creazione prenotazione")
public class PrenotazioneRequest {

    @NotNull(message = "L'ID della casa vacanza è obbligatorio")
    @Schema(description = "ID della casa vacanza", example = "1")
    private Long casaVacanzaId;

    @NotNull(message = "La data di inizio è obbligatoria")
    @Schema(description = "Data di inizio prenotazione", example = "2026-06-01")
    private LocalDate dataInizio;

    @NotNull(message = "La data di fine è obbligatoria")
    @Schema(description = "Data di fine prenotazione", example = "2026-06-07")
    private LocalDate dataFine;

    @NotNull(message = "Il numero di ospiti è obbligatorio")
    @Min(value = 1, message = "Deve esserci almeno 1 ospite")
    @Schema(description = "Numero di ospiti", example = "4")
    private Integer numeroOspiti;
}
