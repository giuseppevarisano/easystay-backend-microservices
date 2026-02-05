package it.easystay.booking.mapper;

import it.easystay.booking.dto.PrenotazioneDTO;
import it.easystay.booking.dto.PrenotazioneResponseDTO;
import it.easystay.booking.model.Prenotazione;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PrenotazioneMapper {
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    Prenotazione toEntity(PrenotazioneDTO dto);
    
    PrenotazioneResponseDTO toResponseDTO(Prenotazione entity);
    
    PrenotazioneDTO toDTO(Prenotazione entity);
}
