package com.easystay.house.service;

import com.easystay.house.dto.CasaVacanzaRequest;
import com.easystay.house.dto.CasaVacanzaResponse;
import com.easystay.house.model.CasaVacanza;
import com.easystay.house.repository.CasaVacanzaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HouseService {

    private final CasaVacanzaRepository casaVacanzaRepository;

    public CasaVacanzaResponse creaCasaVacanza(CasaVacanzaRequest request, Long proprietarioId) {
        CasaVacanza casa = new CasaVacanza();
        casa.setNome(request.getNome());
        casa.setDescrizione(request.getDescrizione());
        casa.setCitta(request.getCitta());
        casa.setIndirizzo(request.getIndirizzo());
        casa.setPrezzoPerNotte(request.getPrezzoPerNotte());
        casa.setNumeroStanze(request.getNumeroStanze());
        casa.setNumeroPostiLetto(request.getNumeroPostiLetto());
        casa.setDisponibile(true);
        casa.setProprietarioId(proprietarioId);

        CasaVacanza saved = casaVacanzaRepository.save(casa);
        return toResponse(saved);
    }

    public List<CasaVacanzaResponse> trovaCaseDisponibiliPerCitta(String citta) {
        return casaVacanzaRepository.findByCittaAndDisponibileTrue(citta)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    public CasaVacanzaResponse trovaCasaById(Long id) {
        CasaVacanza casa = casaVacanzaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Casa vacanza non trovata"));
        return toResponse(casa);
    }

    public List<CasaVacanzaResponse> trovaCasePerProprietario(Long proprietarioId) {
        return casaVacanzaRepository.findByProprietarioId(proprietarioId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private CasaVacanzaResponse toResponse(CasaVacanza casa) {
        return new CasaVacanzaResponse(
                casa.getId(),
                casa.getNome(),
                casa.getDescrizione(),
                casa.getCitta(),
                casa.getIndirizzo(),
                casa.getPrezzoPerNotte(),
                casa.getNumeroStanze(),
                casa.getNumeroPostiLetto(),
                casa.getDisponibile(),
                casa.getProprietarioId(),
                casa.getCreatedAt()
        );
    }
}
