package project.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import project.models.dtos.ClientDTO;

import java.time.LocalDate;
import java.util.List;

/**
 * Rozhraní pro službu pracující s klienty
 */
public interface ClientService extends UserDetailsService {


    /**
     * Vytvoří nového klienta
     * @param dto {@link ClientDTO}
     */
    void createNewClient(ClientDTO dto);


    /**
     * Upraví informace o stávajícím klientovi
     * @param dto {@link ClientDTO}
     */
    void editClient (ClientDTO dto);

    /**
     * Získá informace o klientovi dle jeho identifikátoru
     * @param id Identifikátor klienta
     * @return {@link ClientDTO}
     */
    ClientDTO getClientById (long id);

    /**
     * Získá všechny záznamy o klientech
     * @return záznamy ve formátu {@link ClientDTO}
     */
    List<ClientDTO> getAllClients();

    /**
     * Vypočte věk dle zadaného data narození
     * @param dateOfBirth Datum narození
     * @return Věk
     */
    int calculateAge (LocalDate dateOfBirth);





}
