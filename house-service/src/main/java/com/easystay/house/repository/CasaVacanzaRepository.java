package com.easystay.house.repository;

import com.easystay.house.model.CasaVacanza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CasaVacanzaRepository extends JpaRepository<CasaVacanza, Long> {
    List<CasaVacanza> findByCittaAndDisponibileTrue(String citta);
    List<CasaVacanza> findByProprietarioId(Long proprietarioId);
}
