package project.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.data.entities.ClientEntity;
import project.data.repositories.ClientRepository;
import project.models.dtos.ClientDTO;
import project.models.dtos.mappers.ClientMapper;
import project.models.exceptions.ClientNotFoundException;
import project.models.exceptions.DuplicateEmailException;
import project.models.exceptions.PasswordDoNotEqualException;

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
     * @param dto {@link ClientDTO}
     * @throw  {@link PasswordDoNotEqualException}
     * @throw  {@link DuplicateEmailException}
     */
    @Override
    public void createNewClient(ClientDTO dto) {
        //vyvolání výjimky při chybně zadaném heslu
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new PasswordDoNotEqualException();
        }

        //vyrvoření nové entity
        ClientEntity newClient = new ClientEntity();
        newClient = clientMapper.dtoToEntity(dto);
        newClient.setPassword(passwordEncoder.encode(dto.getPassword())); //heslo neukládám přes mapper, ale ručně (hashování)

        //pokud o uložení (výjimka v případě duplicitního emailu)
        try {
            clientRepository.save(newClient);
        } catch (DataIntegrityViolationException e) {
            throw new DuplicateEmailException();
        }
    }

    /**
     * Upraví informace o klientovi na základě předaných informací
     * @param dto {@link ClientDTO} Nové informace o klientovi
     * @throws ClientNotFoundException Pokud klient není nalezen
     */
    @Override
    public void editClient(ClientDTO dto) {
        ClientEntity fetchedClient = clientRepository
                .findById(dto.getId())
                .orElseThrow(ClientNotFoundException::new);
        clientMapper.updateClientEntity(dto, fetchedClient);
        clientRepository.save(fetchedClient);
    }

    /**
     * Slouží pro načtení informací o klientovi dle definovaného username (emailu)
     * Metoda předepsaná {@link org.springframework.security.core.userdetails.UserDetailsService}
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
     * @param id Identifikátor klienta
     * @return Informace o klientovi ve formátu {@link ClientDTO}
     * @throws ClientNotFoundException Výjimka vyvolaná v případě nenalezení klienta
     */
    @Override
    public ClientDTO getClientById(long id) {
        ClientEntity fetchedClient = clientRepository
                .findById(id)
                .orElseThrow(ClientNotFoundException::new);
        return clientMapper.entityToDTO(fetchedClient);
    }


}
