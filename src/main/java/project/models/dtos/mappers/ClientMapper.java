package project.models.dtos.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import project.data.entities.ClientEntity;
import project.models.dtos.ClientDTO;

/**
 * Rozhraní pro mapování meti objekty {@link ClientDTO} a {@link ClientEntity}
 * Používá knihovnu Mapstruct
 */
@Mapper(componentModel = "spring")
public interface ClientMapper {


    /**
     * Mapuje objekt {@link ClientDTO} na {@link ClientEntity}
     *
     * @param source {@link ClientDTO}
     * @return  {@link ClientEntity}
     */
    @Mapping(target = "password", ignore = true)
    ClientEntity dtoToEntity(ClientDTO source);

    /**
     * Mapuje objekt   {@link ClientEntity} na {@link ClientDTO}
     *
     * @param {@link ClientEntity}
     * @return
     */
    @Mapping(target = "password", ignore = true)
    ClientDTO entityToDTO(ClientEntity sourceEntity);

    /**
     * Aktualizuje {@link ClientDTO} pomocí hodnot z jiného {@link ClientDTO}
     *
     * @param source {@link ClientDTO}
     * @param target {@link ClientDTO}
     */
    void updateClientDTO(ClientDTO source, @MappingTarget ClientDTO target);

    /**
     * Aktualizuje {@link ClientEntity} pomocí zdrojového {@link ClientDTO}
     *
     * @param source {@link ClientEntity}
     * @param target {@link ClientDTO}
     */
    void updateClientEntity(ClientDTO source, @MappingTarget ClientEntity target);
}
