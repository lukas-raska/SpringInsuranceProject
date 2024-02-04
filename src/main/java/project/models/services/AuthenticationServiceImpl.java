package project.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import project.constant.UserRole;
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
     *
     * @param username - Přihlašovací údaj
     * @return Přihlášeného uživatele
     * @throws UsernameNotFoundException - v případě nenalezení uživatele
     */
    @Override
    public UserDetails loadUserByUsername(java.lang.String username) throws UsernameNotFoundException {
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
     * @return - true, pokud je přihlášen
     */
    public boolean isUserLoggedIn() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication instanceof AnonymousAuthenticationToken) {
            return false;
        }
        return true;
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

    @Override
    public String getLoggedInUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();


        if (principal instanceof ClientEntity) {
            return ((ClientEntity) principal).getFirstName() + " " + ((ClientEntity) principal).getLastName();
        }
        if (principal instanceof EmployeeEntity) {
            return ((EmployeeEntity) principal).getFirstName() + " " + ((EmployeeEntity) principal).getLastName();
        }
        return "Unknown user";
    }

    @Override
    public String getLoggedInUserRole() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        if (principal instanceof ClientEntity) {
            return UserRole.ROLE_CLIENT.getName();
        }
        if (principal instanceof EmployeeEntity) {
            return ((EmployeeEntity) principal).isAdmin()? UserRole.ROLE_ADMIN.getName() : UserRole.ROLE_EMPLOYEE.getName();
        }
        return "unknown user role";
    }
}

