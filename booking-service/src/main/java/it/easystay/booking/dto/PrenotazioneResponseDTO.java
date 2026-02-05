package it.easystay.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrenotazioneResponseDTO {
    
    private Long id;
    private LocalDate dataInizio;
    private LocalDate dataFine;
    private Long casaId;
    private Long utenteId;
    private Long version;
}
