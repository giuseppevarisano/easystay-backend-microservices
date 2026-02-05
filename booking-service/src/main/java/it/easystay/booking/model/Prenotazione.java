package it.easystay.booking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "prenotazioni")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prenotazione {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private LocalDate dataInizio;
    
    @Column(nullable = false)
    private LocalDate dataFine;
    
    @Column(nullable = false)
    private Long casaId;
    
    @Column(nullable = false)
    private Long utenteId;
    
    @Version
    private Long version;
}
