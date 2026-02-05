package com.easystay.house.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Risposta casa vacanza")
public class CasaVacanzaResponse {

    @Schema(description = "ID della casa vacanza", example = "1")
    private Long id;

    @Schema(description = "Nome", example = "Villa al Mare")
    private String nome;

    @Schema(description = "Descrizione", example = "Bellissima villa con vista mare")
    private String descrizione;

    @Schema(description = "Città", example = "Roma")
    private String citta;

    @Schema(description = "Indirizzo", example = "Via Roma 123")
    private String indirizzo;

    @Schema(description = "Prezzo per notte", example = "150.00")
    private BigDecimal prezzoPerNotte;

    @Schema(description = "Numero di stanze", example = "3")
    private Integer numeroStanze;

    @Schema(description = "Numero di posti letto", example = "6")
    private Integer numeroPostiLetto;

    @Schema(description = "Disponibilità", example = "true")
    private Boolean disponibile;

    @Schema(description = "ID del proprietario", example = "1")
    private Long proprietarioId;

    @Schema(description = "Data di creazione")
    private LocalDateTime createdAt;
}
