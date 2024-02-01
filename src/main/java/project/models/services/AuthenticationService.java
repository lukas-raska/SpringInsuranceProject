package project.models.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Optional;

public interface AuthenticationService extends UserDetailsService {

    boolean isUserLoggedIn();


    <T extends UserDetails> Optional<T> getLoggedInEntity();
}
