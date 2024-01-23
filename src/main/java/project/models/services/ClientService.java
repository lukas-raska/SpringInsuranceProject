package project.models.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import project.models.dtos.ClientDisplayDTO;
import project.models.dtos.ClientEditDTO;
import project.models.dtos.ClientRegisterDTO;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

/**
 * Rozhraní pro službu pracující s klienty
 */
public interface ClientService extends UserDetailsService {


    /**
     * Vytvoří nového klienta
     * @param dto {@link ClientRegisterDTO}
     */
    void createNewClient(ClientRegisterDTO dto);


    /**
     * Upraví informace o stávajícím klientovi
     * @param dto {@link project.models.dtos.ClientEditDTO}
     */
    void editClient (ClientEditDTO dto);

    /**
     * Získá informace o klientovi dle jeho identifikátoru
     * @param id Identifikátor klienta
     * @return {@link project.models.dtos.ClientDisplayDTO}
     */
    ClientDisplayDTO getClientById (long id);

    /**
     * Získá všechny záznamy o klientech
     * @return záznamy ve formátu {@link ClientDisplayDTO}
     */
    List<ClientDisplayDTO> getAllClients();


    /**
     * Vypočte věk dle zadaného data narození
     * @param dateOfBirth Datum narození
     * @return Věk
     */
    int calculateAge (LocalDate dateOfBirth);







}
