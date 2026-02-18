package com.easystay.booking.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Dettagli utente prenotante")
public class UtenteDettaglioDTO {

    @Schema(description = "ID utente", example = "1")
    private Long id;

    @Schema(description = "Email utente", example = "user@example.com")
    private String email;

    @Schema(description = "Nome utente", example = "Mario Rossi")
    private String nome;

    @Schema(description = "Ruolo utente", example = "USER")
    private String ruolo;

    @Schema(description = "Data creazione utente")
    private LocalDateTime createdAt;
}
