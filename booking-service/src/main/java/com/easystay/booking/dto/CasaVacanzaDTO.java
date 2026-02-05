package com.easystay.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CasaVacanzaDTO {
    private Long id;
    private String nome;
    private String descrizione;
    private String citta;
    private String indirizzo;
    private BigDecimal prezzoPerNotte;
    private Integer numeroStanze;
    private Integer numeroPostiLetto;
    private Boolean disponibile;
    private Long proprietarioId;
}
