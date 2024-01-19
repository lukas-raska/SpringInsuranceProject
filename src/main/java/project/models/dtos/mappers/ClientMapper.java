package project.models.dtos.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import project.data.entities.ClientEntity;
import project.models.dtos.ClientDTO;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    @Mapping(target = "password", ignore = true)
    ClientEntity dtoToEntity(ClientDTO sourceDTO);

@Mapping(target = "password", ignore = true)
    ClientDTO entityToDTO(ClientEntity sourceEntity);

    void updateClientDTO(ClientDTO source, @MappingTarget ClientDTO target);

    void updateClientEntity(ClientDTO source, @MappingTarget ClientEntity target);
}
