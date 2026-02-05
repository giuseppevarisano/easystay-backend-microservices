package it.easystay.booking.repository;

import it.easystay.booking.model.Prenotazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Long> {
    
    List<Prenotazione> findByUtenteId(Long utenteId);
    
    List<Prenotazione> findByCasaId(Long casaId);
    
    @Query("SELECT p FROM Prenotazione p WHERE p.casaId = :casaId " +
           "AND p.id != :excludeId " +
           "AND ((p.dataInizio <= :dataFine AND p.dataFine >= :dataInizio))")
    List<Prenotazione> findOverlappingBookings(
            @Param("casaId") Long casaId,
            @Param("dataInizio") LocalDate dataInizio,
            @Param("dataFine") LocalDate dataFine,
            @Param("excludeId") Long excludeId
    );
}
