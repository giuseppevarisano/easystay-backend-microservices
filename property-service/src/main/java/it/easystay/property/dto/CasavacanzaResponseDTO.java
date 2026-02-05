package it.easystay.property.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CasavacanzaResponseDTO {
    
    private Long id;
    private String nome;
    private String indirizzo;
    private Double prezzoNotte;
    private String citta;
    private Long version;
}
