package project.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.data.entities.ClientEntity;
import project.data.repositories.ClientRepository;
import project.models.dtos.UserDisplayDTO;
import project.models.dtos.UserEditDTO;
import project.models.dtos.UserRegisterDTO;
import project.models.dtos.mappers.UserMapper;
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
    private UserMapper userMapper;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Vytvoří nové klienta
     *
     * @param dto {@link UserRegisterDTO}
     * @throws PasswordDoNotEqualException
     * @throws DuplicateEmailException
     */
    @Override
    public void create(UserRegisterDTO dto) {
        //vyvolání výjimky při chybně zadaném heslu
        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new PasswordDoNotEqualException();
        }
        //vytvoření nové entity
        ClientEntity newClient = userMapper.mapToClient(dto);
        newClient.setPassword(passwordEncoder.encode(dto.getPassword())); //heslo neukládám přes mapper, ale ručně (hashování)
        newClient.setDateOfRegistration(LocalDate.now());

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
     * @param dto {@link UserEditDTO} Nové informace o klientovi
     * @throws ClientNotFoundException Pokud klient není nalezen
     */
    @Override
    public void edit(UserEditDTO dto) {
        ClientEntity fetchedClient = clientRepository
                .findById(dto.getId())
                .orElseThrow(ClientNotFoundException::new);
        userMapper.updateClientEntity(dto, fetchedClient);
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


    /**
     * Získá informace o klientovi dle jeho identifikátoru
     *
     * @param id Identifikátor klienta
     * @return Informace o klientovi ve formátu {@link UserDisplayDTO}
     * @throws ClientNotFoundException Výjimka vyvolaná v případě nenalezení klienta
     */
    @Override
    public UserDisplayDTO getById(long id) {
        ClientEntity fetchedClient = clientRepository
                .findById(id)
                .orElseThrow(ClientNotFoundException::new);
        return userMapper.mapToDTO(fetchedClient);
    }

    /**
     * Slouží pro získání všech záznamů {@link ClientEntity} z databáze
     *
     * @return Seznam všech záznamů
     */
    @Override
    public List<UserDisplayDTO> getAll() {
        return clientRepository
                .findAll()
                .stream()
                .map(entity -> userMapper.mapToDTO(entity))
                .peek(client -> client.setAge(getAge(client.getDateOfBirth())))
                .toList();
    }


    @Override
    public void remove(long id) {
        clientRepository.deleteById(id);
    }

    /**
     * Slouží pro výpočet věku klienta dle data narození
     *
     * @param dateOfBirth
     * @return Věk klienta
     */
    @Override
    public int getAge(LocalDate dateOfBirth) {
        return Period
                .between(dateOfBirth, LocalDate.now())
                .getYears();
    }
}

