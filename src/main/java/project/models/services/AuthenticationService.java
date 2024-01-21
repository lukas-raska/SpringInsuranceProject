package project.models.services;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

/**
 * Služba pro získávání informací o aktuálně přihlášeném uživateli
 */
@Service
public class AuthenticationService {


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

