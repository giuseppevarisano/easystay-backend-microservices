package it.easystay.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrenotazioneDTO {
    
    @NotNull(message = "Data inizio is required")
    private LocalDate dataInizio;
    
    @NotNull(message = "Data fine is required")
    private LocalDate dataFine;
    
    @NotNull(message = "Casa ID is required")
    private Long casaId;
    
    @NotNull(message = "Utente ID is required")
    private Long utenteId;
}
