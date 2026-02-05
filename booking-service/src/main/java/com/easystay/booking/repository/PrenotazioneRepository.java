package com.easystay.booking.repository;

import com.easystay.booking.model.Prenotazione;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {
    
    Page<Prenotazione> findByUtenteId(Long utenteId, Pageable pageable);

    List<Prenotazione> findByCasaVacanzaId(Long casaVacanzaId);

    @Query("SELECT p FROM Prenotazione p WHERE p.casaVacanzaId = :casaId " +
           "AND p.stato = 'CONFERMATA' " +
           "AND ((p.dataInizio <= :dataFine AND p.dataFine >= :dataInizio))")
    List<Prenotazione> findPrenotazioniSovrapposte(
            @Param("casaId") Long casaId,
            @Param("dataInizio") LocalDate dataInizio,
            @Param("dataFine") LocalDate dataFine
    );
}
