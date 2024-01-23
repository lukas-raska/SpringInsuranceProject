package project.models.dtos.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import project.data.entities.ClientEntity;
import project.models.dtos.ClientDisplayDTO;
import project.models.dtos.ClientEditDTO;
import project.models.dtos.ClientRegisterDTO;

/**
 * Rozhraní pro mapování meti objekty {@link ClientRegisterDTO} a {@link ClientEntity}
 * Používá knihovnu Mapstruct
 */
@Mapper(componentModel = "spring")
public interface ClientMapper {


    /**
     * Mapuje objekt {@link ClientRegisterDTO} na {@link ClientEntity}
     *
     * @param source {@link ClientRegisterDTO}
     * @return  {@link ClientEntity}
     */
    @Mapping(target = "password", ignore = true)
    ClientEntity dtoToEntity(ClientRegisterDTO source);


    /**
     * Mapuje objekt {@link ClientEditDTO} na {@link ClientEntity}
     * @param source {@link ClientEditDTO}
     * @return {@link ClientEntity}
     */
    ClientEntity dtoToEntity(ClientEditDTO source);


    /**
     * Mapuje objekt   {@link ClientEntity} na {@link ClientDisplayDTO}
     *
     * @param {@link ClientEntity}
     * @return
     */

    ClientDisplayDTO entityToDTO(ClientEntity sourceEntity);

    /**
     * Aktualizuje {@link ClientEditDTO} pomocí hodnot z jiného {@link ClientEditDTO}
     *
     * @param source {@link ClientEditDTO}
     * @param target {@link ClientEditDTO}
     */
    void updateClientDTO(ClientEditDTO source, @MappingTarget ClientEditDTO target);

    /**
     * Aktualizuje {@link ClientEditDTO} pomocí hodnot z jiného {@link ClientDisplayDTO}
     *
     * @param source {@link ClientDisplayDTO}
     * @param target {@link ClientEditDTO}
     */
    void updateClientDTO(ClientDisplayDTO source, @MappingTarget ClientEditDTO target);


    /**
     * Aktualizuje {@link ClientEntity} pomocí zdrojového {@link ClientEditDTO}
     *
     * @param source {@link ClientEntity}
     * @param target {@link ClientEditDTO}
     */
    void updateClientEntity(ClientEditDTO source, @MappingTarget ClientEntity target);
}
