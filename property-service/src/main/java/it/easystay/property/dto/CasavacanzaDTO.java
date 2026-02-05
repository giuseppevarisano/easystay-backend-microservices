package it.easystay.property.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CasavacanzaDTO {
    
    @NotBlank(message = "Il nome è obbligatorio")
    private String nome;
    
    @NotBlank(message = "L'indirizzo è obbligatorio")
    private String indirizzo;
    
    @NotNull(message = "Il prezzo per notte è obbligatorio")
    @Positive(message = "Il prezzo per notte deve essere positivo")
    private Double prezzoNotte;
    
    @NotBlank(message = "La città è obbligatoria")
    private String citta;
}
