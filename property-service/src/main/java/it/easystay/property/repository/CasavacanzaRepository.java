package it.easystay.property.repository;

import it.easystay.property.model.Casavacanza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CasavacanzaRepository extends JpaRepository<Casavacanza, Long> {
    List<Casavacanza> findByCitta(String citta);
}
