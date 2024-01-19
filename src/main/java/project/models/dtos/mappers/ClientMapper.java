package project.models.dtos.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import project.data.entities.ClientEntity;
import project.models.dtos.ClientEditDTO;
import project.models.dtos.ClientRegistrationDTO;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    @Mapping(target = "password", ignore = true)
    ClientEntity dtoToEntity(ClientRegistrationDTO sourceDTO);




    ClientEditDTO entityToDTO(ClientEntity sourceEntity);

    void updateClientDTO(ClientEditDTO source, @MappingTarget ClientEditDTO target);

    void updateClientEntity(ClientEditDTO source, @MappingTarget ClientEntity target);
}
