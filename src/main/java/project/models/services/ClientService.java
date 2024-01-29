package project.models.services;

import org.springframework.security.core.userdetails.UserDetailsService;
import project.models.dtos.ClientDisplayDTO;
import project.models.dtos.ClientEditDTO;
import project.models.dtos.client.ClientRegisterDTO;

import java.time.LocalDate;
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
     * @param dto {@link ClientEditDTO}
     */
    void editClient (ClientEditDTO dto);

    /**
     * Získá informace o klientovi dle jeho identifikátoru
     * @param id Identifikátor klienta
     * @return {@link ClientDisplayDTO}
     */
    ClientDisplayDTO getClientById (long id);

    /**
     * Získá všechny záznamy o klientech
     * @return záznamy ve formátu {@link ClientDisplayDTO}
     */
    List<ClientDisplayDTO> getAllClients();

    /**
     * Odstraní záznam o klientovi z databáze
     * @param id
     */
    void deleteClient (long id);

    /**
     * Vypočte věk dle zadaného data narození
     * @param dateOfBirth Datum narození
     * @return Věk
     */
    int calculateAge (LocalDate dateOfBirth);









}
