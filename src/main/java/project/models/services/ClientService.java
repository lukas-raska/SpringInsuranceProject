package project.models.services;


import project.models.dtos.UserDisplayDTO;
import project.models.dtos.UserEditDTO;
import project.models.dtos.UserRegisterDTO;

import java.time.LocalDate;
import java.util.List;

/**
 * Rozhraní pro službu pracující s klienty
 */
public interface ClientService {//extends UserDetailsService


    /**
     * Vytvoří nového klienta
     * @param dto {@link UserRegisterDTO}
     */
    void create(UserRegisterDTO dto);


    /**
     * Upraví informace o stávajícím klientovi
     * @param dto {@link UserEditDTO}
     */
    void edit(UserEditDTO dto);

    /**
     * Získá informace o klientovi dle jeho identifikátoru
     * @param id Identifikátor klienta
     * @return {@link UserDisplayDTO}
     */
    UserDisplayDTO getById(long id);

    /**
     * Získá všechny záznamy o klientech
     * @return záznamy ve formátu {@link project.models.dtos.UserDisplayDTO}
     */
    List<UserDisplayDTO> getAll();

    /**
     * Odstraní záznam o klientovi z databáze
     * @param id
     */
    void remove(long id);

    /**
     * Vypočte věk dle zadaného data narození
     * @param dateOfBirth Datum narození
     * @return Věk
     */
    int getAge(LocalDate dateOfBirth);









}
