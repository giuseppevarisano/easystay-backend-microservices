package com.easystay.booking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dettaglio casa con prenotazioni e utenti prenotanti")
public class PrenotazioniCasaDettaglioResponse {

    @Schema(description = "Dettagli casa vacanza")
    private CasaVacanzaDTO casa;

    @Schema(description = "Prenotazioni associate con utente prenotante")
    private List<PrenotazioneConUtenteResponse> prenotazioni;
}
