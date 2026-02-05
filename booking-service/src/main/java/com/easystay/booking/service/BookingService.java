package com.easystay.booking.service;

import com.easystay.booking.client.HouseServiceClient;
import com.easystay.booking.dto.CasaVacanzaDTO;
import com.easystay.booking.dto.PrenotazioneRequest;
import com.easystay.booking.dto.PrenotazioneResponse;
import com.easystay.booking.model.Prenotazione;
import com.easystay.booking.repository.PrenotazioneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final PrenotazioneRepository prenotazioneRepository;
    private final HouseServiceClient houseServiceClient;

    public PrenotazioneResponse creaPrenotazione(PrenotazioneRequest request, Long utenteId, String token) {
        // Validazione date
        if (request.getDataFine().isBefore(request.getDataInizio()) || 
            request.getDataFine().isEqual(request.getDataInizio())) {
            throw new RuntimeException("Date incoerenti");
        }

        // Recupera la casa vacanza
        CasaVacanzaDTO casa = houseServiceClient.getCasaVacanzaById(request.getCasaVacanzaId(), token);

        if (!casa.getDisponibile()) {
            throw new RuntimeException("Casa vacanza non disponibile");
        }

        if (request.getNumeroOspiti() > casa.getNumeroPostiLetto()) {
            throw new RuntimeException("Numero di ospiti superiore alla capienza");
        }

        // Verifica sovrapposizione prenotazioni
        List<Prenotazione> sovrapposte = prenotazioneRepository.findPrenotazioniSovrapposte(
                request.getCasaVacanzaId(),
                request.getDataInizio(),
                request.getDataFine()
        );

        if (!sovrapposte.isEmpty()) {
            throw new RuntimeException("Casa vacanza già prenotata per le date selezionate");
        }

        // Calcola prezzo totale
        long giorni = ChronoUnit.DAYS.between(request.getDataInizio(), request.getDataFine());
        BigDecimal prezzoTotale = casa.getPrezzoPerNotte().multiply(BigDecimal.valueOf(giorni));

        // Crea prenotazione
        Prenotazione prenotazione = new Prenotazione();
        prenotazione.setUtenteId(utenteId);
        prenotazione.setCasaVacanzaId(request.getCasaVacanzaId());
        prenotazione.setDataInizio(request.getDataInizio());
        prenotazione.setDataFine(request.getDataFine());
        prenotazione.setNumeroOspiti(request.getNumeroOspiti());
        prenotazione.setPrezzoTotale(prezzoTotale);
        prenotazione.setStato(Prenotazione.StatoPrenotazione.CONFERMATA);

        Prenotazione saved = prenotazioneRepository.save(prenotazione);
        return toResponse(saved);
    }

    public Page<PrenotazioneResponse> trovaPrenotazioniUtente(Long utenteId, Pageable pageable) {
        return prenotazioneRepository.findByUtenteId(utenteId, pageable)
                .map(this::toResponse);
    }

    private PrenotazioneResponse toResponse(Prenotazione prenotazione) {
        return new PrenotazioneResponse(
                prenotazione.getId(),
                prenotazione.getUtenteId(),
                prenotazione.getCasaVacanzaId(),
                prenotazione.getDataInizio(),
                prenotazione.getDataFine(),
                prenotazione.getNumeroOspiti(),
                prenotazione.getPrezzoTotale(),
                prenotazione.getStato().name(),
                prenotazione.getCreatedAt()
        );
    }
}
