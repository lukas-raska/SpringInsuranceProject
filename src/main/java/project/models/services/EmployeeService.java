package project.models.services;

import project.models.dtos.UserDisplayDTO;
import project.models.dtos.UserEditDTO;
import project.models.dtos.UserRegisterDTO;

import java.time.LocalDate;
import java.util.List;

/**
 * Interface předepisující service metody pro práci se zaměstnanci
 */
public interface EmployeeService {

    /**
     * Vytvoří nového zaměstnance
     * @param dto {@link UserRegisterDTO}
     */
    void create(UserRegisterDTO dto);


    /**
     * Upraví informace o stávajícím zaměstnanci
     * @param dto {@link UserEditDTO}
     */
    void edit(UserEditDTO dto);

    /**
     * Získá informace o zaměstnanci dle jeho identifikátoru
     * @param id Identifikátor zaměstnance
     * @return {@link UserDisplayDTO}
     */
    UserDisplayDTO getById(long id);

    /**
     * Získá všechny záznamy o zaměstnancích
     * @return záznamy ve formátu {@link project.models.dtos.UserDisplayDTO}
     */
    List<UserDisplayDTO> getAll();

    /**
     * Odstraní záznam o zaměstnanci z databáze
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
