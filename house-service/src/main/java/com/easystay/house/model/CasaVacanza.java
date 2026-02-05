package com.easystay.house.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "case_vacanza")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CasaVacanza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String descrizione;

    @Column(nullable = false)
    private String citta;

    @Column(nullable = false)
    private String indirizzo;

    @Column(nullable = false)
    private BigDecimal prezzoPerNotte;

    @Column(nullable = false)
    private Integer numeroStanze;

    @Column(nullable = false)
    private Integer numeroPostiLetto;

    @Column(nullable = false)
    private Boolean disponibile = true;

    @Column(name = "proprietario_id", nullable = false)
    private Long proprietarioId;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
