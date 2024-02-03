package project.models.dtos.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import project.data.entities.ClientEntity;
import project.data.entities.EmployeeEntity;
import project.models.dtos.UserDisplayDTO;
import project.models.dtos.UserEditDTO;
import project.models.dtos.UserRegisterDTO;


/**
 * Rozhraní pro mapování mezi DTO a entitami
 * Používá knihovnu Mapstruct
 */
@Mapper(componentModel = "spring")
public interface UserMapper {


    /**
     * Mapuje objekt DTO na entitu
     *
     * @param source {@link UserRegisterDTO}
     * @return {@link ClientEntity}
     */
    @Mapping(target = "password", ignore = true)
    ClientEntity mapToClient(UserRegisterDTO source);

    /**
     * Mapuje objekt {@link UserRegisterDTO} na {@link EmployeeEntity}
     * @param source {@link UserRegisterDTO}
     * @return {@link EmployeeEntity}
     */
    EmployeeEntity mapToEmployee(UserRegisterDTO source);


    /**
     * Mapuje objekt {@link UserEditDTO} na {@link ClientEntity}
     *
     * @param source {@link UserEditDTO}
     * @return {@link ClientEntity}
     */
    ClientEntity mapToClient(UserEditDTO source);


    /**
     * Mapuje objekt   {@link ClientEntity} na {@link UserDisplayDTO}
     *
     * @param {@link ClientEntity}
     * @return
     */
    UserDisplayDTO mapToDTO(ClientEntity source);

    /**
     * Mapuje objekt {@link EmployeeEntity} na {@link UserDisplayDTO}
     * @param source - {@link EmployeeEntity}
     * @return - {@link UserDisplayDTO}
     */
    UserDisplayDTO mapToDTO(EmployeeEntity source);


    /**
     * Aktualizuje {@link UserEditDTO} pomocí hodnot z jiného {@link UserEditDTO}
     *
     * @param source {@link UserEditDTO}
     * @param target {@link UserEditDTO}
     */
    void updateUserDTO(UserEditDTO source, @MappingTarget UserEditDTO target);

    /**
     * Aktualizuje {@link UserEditDTO} pomocí hodnot z jiného {@link UserDisplayDTO}
     *
     * @param source {@link UserDisplayDTO}
     * @param target {@link UserEditDTO}
     */
    void updateUserDTO(UserDisplayDTO source, @MappingTarget UserEditDTO target);


    /**
     * Aktualizuje {@link ClientEntity} pomocí zdrojového {@link UserEditDTO}
     *
     * @param source {@link ClientEntity}
     * @param target {@link UserEditDTO}
     */
    void updateClientEntity(UserEditDTO source, @MappingTarget ClientEntity target);

    /**
     * Aktualizuje objekt {@link EmployeeEntity} pomocí zdrojového DTO {@link UserEditDTO}
     * @param source - {@link UserEditDTO}
     * @param target - {@link EmployeeEntity}
     */
    void updateEmployeeEntity(UserEditDTO source, @MappingTarget EmployeeEntity target);
}
