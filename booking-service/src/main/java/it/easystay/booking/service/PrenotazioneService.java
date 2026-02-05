package it.easystay.booking.service;

import feign.FeignException;
import it.easystay.booking.client.PropertyClient;
import it.easystay.booking.dto.PrenotazioneDTO;
import it.easystay.booking.dto.PrenotazioneResponseDTO;
import it.easystay.booking.mapper.PrenotazioneMapper;
import it.easystay.booking.model.Prenotazione;
import it.easystay.booking.repository.PrenotazioneRepository;
import it.easystay.common.exception.BadRequestException;
import it.easystay.common.exception.ResourceNotFoundException;
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
public class PrenotazioneService {
    
    private final PrenotazioneRepository prenotazioneRepository;
    private final PrenotazioneMapper prenotazioneMapper;
    private final PropertyClient propertyClient;
    
    public PrenotazioneResponseDTO create(PrenotazioneDTO dto) {
        log.debug("Creating new booking: {}", dto);
        
        validateDates(dto.getDataInizio(), dto.getDataFine());
        validatePropertyExists(dto.getCasaId());
        checkOverlappingBookings(dto.getCasaId(), dto.getDataInizio(), dto.getDataFine(), null);
        
        Prenotazione prenotazione = prenotazioneMapper.toEntity(dto);
        Prenotazione saved = prenotazioneRepository.save(prenotazione);
        
        log.debug("Booking created with ID: {}", saved.getId());
        return prenotazioneMapper.toResponseDTO(saved);
    }
    
    @Transactional(readOnly = true)
    public List<PrenotazioneResponseDTO> getAll() {
        log.debug("Fetching all bookings");
        return prenotazioneRepository.findAll().stream()
                .map(prenotazioneMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public PrenotazioneResponseDTO getById(Long id) {
        log.debug("Fetching booking with ID: {}", id);
        Prenotazione prenotazione = prenotazioneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prenotazione with id " + id + " not found"));
        return prenotazioneMapper.toResponseDTO(prenotazione);
    }
    
    @Transactional(readOnly = true)
    public List<PrenotazioneResponseDTO> findByUtenteId(Long utenteId) {
        log.debug("Fetching bookings for user ID: {}", utenteId);
        return prenotazioneRepository.findByUtenteId(utenteId).stream()
                .map(prenotazioneMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<PrenotazioneResponseDTO> findByCasaId(Long casaId) {
        log.debug("Fetching bookings for property ID: {}", casaId);
        return prenotazioneRepository.findByCasaId(casaId).stream()
                .map(prenotazioneMapper::toResponseDTO)
                .collect(Collectors.toList());
    }
    
    public PrenotazioneResponseDTO update(Long id, PrenotazioneDTO dto) {
        log.debug("Updating booking with ID: {}", id);
        
        Prenotazione existing = prenotazioneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prenotazione with id " + id + " not found"));
        
        validateDates(dto.getDataInizio(), dto.getDataFine());
        validatePropertyExists(dto.getCasaId());
        checkOverlappingBookings(dto.getCasaId(), dto.getDataInizio(), dto.getDataFine(), id);
        
        existing.setDataInizio(dto.getDataInizio());
        existing.setDataFine(dto.getDataFine());
        existing.setCasaId(dto.getCasaId());
        existing.setUtenteId(dto.getUtenteId());
        
        Prenotazione updated = prenotazioneRepository.save(existing);
        
        log.debug("Booking updated: {}", updated.getId());
        return prenotazioneMapper.toResponseDTO(updated);
    }
    
    public void delete(Long id) {
        log.debug("Deleting booking with ID: {}", id);
        
        if (!prenotazioneRepository.existsById(id)) {
            throw new ResourceNotFoundException("Prenotazione with id " + id + " not found");
        }
        
        prenotazioneRepository.deleteById(id);
        log.debug("Booking deleted: {}", id);
    }
    
    private void validateDates(java.time.LocalDate dataInizio, java.time.LocalDate dataFine) {
        if (dataInizio.isAfter(dataFine)) {
            throw new BadRequestException("Data inizio must be before data fine");
        }
        if (dataInizio.isBefore(java.time.LocalDate.now())) {
            throw new BadRequestException("Data inizio cannot be in the past");
        }
    }
    
    private void validatePropertyExists(Long casaId) {
        try {
            propertyClient.getPropertyById(casaId);
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException("Casavacanza with id " + casaId + " not found");
        } catch (FeignException e) {
            log.error("Error communicating with property service", e);
            throw new BadRequestException("Unable to validate property: " + e.getMessage());
        }
    }
    
    private void checkOverlappingBookings(Long casaId, java.time.LocalDate dataInizio, 
                                         java.time.LocalDate dataFine, Long excludeId) {
        Long idToExclude = excludeId != null ? excludeId : -1L;
        List<Prenotazione> overlapping = prenotazioneRepository.findOverlappingBookings(
                casaId, dataInizio, dataFine, idToExclude);
        
        if (!overlapping.isEmpty()) {
            throw new BadRequestException("Property is already booked for the selected dates");
        }
    }
}
