package it.easystay.property.service;

import it.easystay.common.exception.ResourceNotFoundException;
import it.easystay.property.dto.CasavacanzaDTO;
import it.easystay.property.dto.CasavacanzaResponseDTO;
import it.easystay.property.mapper.CasavacanzaMapper;
import it.easystay.property.model.Casavacanza;
import it.easystay.property.repository.CasavacanzaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CasavacanzaService {
    
    private final CasavacanzaRepository casavacanzaRepository;
    private final CasavacanzaMapper casavacanzaMapper;
    
    public CasavacanzaResponseDTO create(CasavacanzaDTO dto) {
        log.debug("Creating new casavacanza: {}", dto);
        Casavacanza casavacanza = casavacanzaMapper.toEntity(dto);
        Casavacanza saved = casavacanzaRepository.save(casavacanza);
        log.info("Created casavacanza with id: {}", saved.getId());
        return casavacanzaMapper.toResponseDTO(saved);
    }
    
    @Transactional(readOnly = true)
    public List<CasavacanzaResponseDTO> getAll() {
        log.debug("Fetching all casevacanza");
        return casavacanzaRepository.findAll()
                .stream()
                .map(casavacanzaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public CasavacanzaResponseDTO getById(Long id) {
        log.debug("Fetching casavacanza with id: {}", id);
        Casavacanza casavacanza = casavacanzaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Casavacanza non trovata con id: " + id));
        return casavacanzaMapper.toResponseDTO(casavacanza);
    }
    
    @Transactional(readOnly = true)
    public List<CasavacanzaResponseDTO> findByCitta(String citta) {
        log.debug("Fetching casevacanza in city: {}", citta);
        return casavacanzaRepository.findByCitta(citta)
                .stream()
                .map(casavacanzaMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    public CasavacanzaResponseDTO update(Long id, CasavacanzaDTO dto) {
        log.debug("Updating casavacanza with id: {}", id);
        Casavacanza casavacanza = casavacanzaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Casavacanza non trovata con id: " + id));
        
        casavacanzaMapper.updateEntityFromDTO(dto, casavacanza);
        Casavacanza updated = casavacanzaRepository.save(casavacanza);
        log.info("Updated casavacanza with id: {}", updated.getId());
        return casavacanzaMapper.toResponseDTO(updated);
    }
    
    public void delete(Long id) {
        log.debug("Deleting casavacanza with id: {}", id);
        if (!casavacanzaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Casavacanza non trovata con id: " + id);
        }
        casavacanzaRepository.deleteById(id);
        log.info("Deleted casavacanza with id: {}", id);
    }
}
