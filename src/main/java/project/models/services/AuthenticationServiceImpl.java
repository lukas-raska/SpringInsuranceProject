package project.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project.data.entities.ClientEntity;
import project.data.entities.EmployeeEntity;
import project.data.repositories.ClientRepository;
import project.data.repositories.EmployeeRepository;

import java.util.Optional;

/**
 * Služba pro získávání informací o aktuálně přihlášeném uživateli
 */
@SuppressWarnings("unchecked")
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * Načte uživatele dle username definovaného pro login
     * Metoda předepsaná {@link org.springframework.security.core.userdetails.UserDetailsService}
     * @param username - Přihlašovací údaj
     * @return Přihlášeného uživatele
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //přihlašování umožněno 2 entitám
        //prioritní vyhledávání nastaveno pro EmployeeEntity
        Optional<EmployeeEntity> employee = employeeRepository.findByEmail(username);
        if (employee.isPresent()) {
            return employee.get();
        }
        //pokud není nalezen zaměstnanec, vyhledává se mezi ClientEntity
        Optional<ClientEntity> client = clientRepository.findByEmail(username);
        if (client.isPresent()) {
            return client.get();

        }
        //pokud user není nalezen vůbec, hodí výjimku
        throw new UsernameNotFoundException("Username: " + username + " not found in database");
    }

    /**
     * Zjišťuje, zda je uživatel aktuálně přihlášen
     *
     * @return
     */
    public boolean isUserLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated();
    }

    /**
     * Získává informace o aktuálně přihlášeném uživateli
     *
     * @param <T> Typ UserDetails či jeho potomci
     * @return Optional hodnota obsahující inforamce o aktuálně přihlášené entitě
     */
    public <T extends UserDetails> Optional<T> getLoggedInEntity() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (isUserLoggedIn() && principal instanceof UserDetails) {
            return Optional.of((T) principal);
        }
        return Optional.empty();
    }

}

