package it.easystay.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CasavacanzaDTO {
    
    private Long id;
    private String nome;
    private String indirizzo;
    private Double prezzoNotte;
    private String citta;
    private Long version;
}
