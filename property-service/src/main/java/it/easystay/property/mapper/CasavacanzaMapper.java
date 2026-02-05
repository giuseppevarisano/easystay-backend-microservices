package it.easystay.property.mapper;

import it.easystay.property.dto.CasavacanzaDTO;
import it.easystay.property.dto.CasavacanzaResponseDTO;
import it.easystay.property.model.Casavacanza;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CasavacanzaMapper {
    
    Casavacanza toEntity(CasavacanzaDTO dto);
    
    CasavacanzaResponseDTO toResponseDTO(Casavacanza entity);
    
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "version", ignore = true)
    void updateEntityFromDTO(CasavacanzaDTO dto, @MappingTarget Casavacanza entity);
}
