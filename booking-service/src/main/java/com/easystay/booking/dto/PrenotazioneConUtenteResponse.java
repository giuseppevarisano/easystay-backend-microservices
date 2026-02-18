package com.easystay.booking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Prenotazione arricchita con dettagli utente")
public class PrenotazioneConUtenteResponse {

    @Schema(description = "Dettagli prenotazione")
    private PrenotazioneResponse prenotazione;

    @Schema(description = "Dettagli utente prenotante")
    private UtenteDettaglioDTO utente;
}
