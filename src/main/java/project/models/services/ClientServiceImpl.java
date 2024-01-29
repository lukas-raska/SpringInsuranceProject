package project.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.data.entities.ClientEntity;
import project.data.repositories.ClientRepository;
import project.models.dtos.ClientDisplayDTO;
import project.models.dtos.ClientEditDTO;
import project.models.dtos.client.ClientRegisterDTO;
import project.models.dtos.mappers.ClientMapper;
import project.models.exceptions.ClientNotFoundException;
import project.models.exceptions.DuplicateEmailException;
import project.models.exceptions.PasswordDoNotEqualException;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

/**
 * Implementace rozhraní {@link ClientService} pro manipulaci s klienty
 */
@Service
public class ClientServiceImpl implements ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private ClientMapper clientMapper;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Vytvoří nové klienta
     *
     * @param dto {@link ClientRegisterDTO}
     * @throws PasswordDoNotEqualException
     * @throws DuplicateEmailException
     */
    @Override
    public void createNewClient(ClientRegisterDTO dto) {
        //vyvolání výjimky při chybně zadaném heslu
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new PasswordDoNotEqualException();
        }

        //vyrvoření nové entity
        ClientEntity newClient = new ClientEntity();
        newClient = clientMapper.dtoToEntity(dto);
        newClient.setPassword(passwordEncoder.encode(dto.getPassword())); //heslo neukládám přes mapper, ale ručně (hashování)
        newClient.setRegistrationDate(LocalDate.now());

        //pokud o uložení (výjimka v případě duplicitního emailu)
        try {
            clientRepository.save(newClient);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEmailException();
        }
    }

    /**
     * Upraví informace o klientovi na základě předaných informací
     *
     * @param dto {@link ClientEditDTO} Nové informace o klientovi
     * @throws ClientNotFoundException Pokud klient není nalezen
     */
    @Override
    public void editClient(ClientEditDTO dto) {
        ClientEntity fetchedClient = clientRepository
                .findById(dto.getId())
                .orElseThrow(ClientNotFoundException::new);
        clientMapper.updateClientEntity(dto, fetchedClient);
        clientRepository.save(fetchedClient);
    }

    /**
     * Slouží pro načtení informací o klientovi dle definovaného username (emailu)
     * Metoda předepsaná {@link org.springframework.security.core.userdetails.UserDetailsService}
     *
     * @param username (email klienta)
     * @return Informace o klientovi ve formátu {@link UserDetails}
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return clientRepository
                .findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found"));
    }

    /**
     * Získá informace o klientovi dle jeho identifikátoru
     *
     * @param id Identifikátor klienta
     * @return Informace o klientovi ve formátu {@link ClientDisplayDTO}
     * @throws ClientNotFoundException Výjimka vyvolaná v případě nenalezení klienta
     */
    @Override
    public ClientDisplayDTO getClientById(long id) {
        ClientEntity fetchedClient = clientRepository
                .findById(id)
                .orElseThrow(ClientNotFoundException::new);
        return clientMapper.entityToDTO(fetchedClient);
    }

    /**
     * Slouží pro získání všech záznamů {@link ClientEntity} z databáze
     *
     * @return Seznam všech záznamů
     */
    @Override
    public List<ClientDisplayDTO> getAllClients() {

        return clientRepository
                .findAll()
                .stream()
                .map(entity -> clientMapper.entityToDTO(entity))
                .peek(client -> client.setAge(calculateAge(client.getDateOfBirth())))
                .toList();
    }


    @Override
    public void deleteClient(long id) {
        clientRepository.deleteById(id);
    }

    /**
     * Slouží pro výpočet věku klienta dle data narození
     *
     * @param dateOfBirth
     * @return Věk klienta
     */
    @Override
    public int calculateAge(LocalDate dateOfBirth) {
        return Period
                .between(dateOfBirth, LocalDate.now())
                .getYears();
    }
}

