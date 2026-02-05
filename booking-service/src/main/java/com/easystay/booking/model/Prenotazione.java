package com.easystay.booking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "prenotazioni")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prenotazione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "utente_id", nullable = false)
    private Long utenteId;

    @Column(name = "casa_vacanza_id", nullable = false)
    private Long casaVacanzaId;

    @Column(name = "data_inizio", nullable = false)
    private LocalDate dataInizio;

    @Column(name = "data_fine", nullable = false)
    private LocalDate dataFine;

    @Column(name = "numero_ospiti", nullable = false)
    private Integer numeroOspiti;

    @Column(name = "prezzo_totale", nullable = false)
    private BigDecimal prezzoTotale;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatoPrenotazione stato = StatoPrenotazione.CONFERMATA;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum StatoPrenotazione {
        CONFERMATA, CANCELLATA, COMPLETATA
    }
}
