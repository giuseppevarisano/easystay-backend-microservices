package com.easystay.house.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Schema(description = "Richiesta creazione casa vacanza")
public class CasaVacanzaRequest {

    @NotBlank(message = "Il nome è obbligatorio")
    @Schema(description = "Nome della casa vacanza", example = "Villa al Mare")
    private String nome;

    @NotBlank(message = "La descrizione è obbligatoria")
    @Schema(description = "Descrizione della casa", example = "Bellissima villa con vista mare")
    private String descrizione;

    @NotBlank(message = "La città è obbligatoria")
    @Schema(description = "Città", example = "Roma")
    private String citta;

    @NotBlank(message = "L'indirizzo è obbligatorio")
    @Schema(description = "Indirizzo completo", example = "Via Roma 123")
    private String indirizzo;

    @NotNull(message = "Il prezzo per notte è obbligatorio")
    @DecimalMin(value = "0.0", inclusive = false, message = "Il prezzo deve essere maggiore di zero")
    @Schema(description = "Prezzo per notte", example = "150.00")
    private BigDecimal prezzoPerNotte;

    @NotNull(message = "Il numero di stanze è obbligatorio")
    @Min(value = 1, message = "Deve avere almeno 1 stanza")
    @Schema(description = "Numero di stanze", example = "3")
    private Integer numeroStanze;

    @NotNull(message = "Il numero di posti letto è obbligatorio")
    @Min(value = 1, message = "Deve avere almeno 1 posto letto")
    @Schema(description = "Numero di posti letto", example = "6")
    private Integer numeroPostiLetto;
}
