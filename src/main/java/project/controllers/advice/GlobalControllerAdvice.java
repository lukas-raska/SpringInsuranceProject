package project.controllers.advice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ModelAndView;
import project.data.entities.ClientEntity;
import project.models.dtos.ClientDTO;
import project.models.dtos.mappers.ClientMapper;
import project.models.services.AuthenticationService;

import java.util.Optional;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ClientMapper clientMapper;

    @ModelAttribute
    public void loggedInUser(Model model) {
        Optional<UserDetails> loggedIn = authenticationService.getLoggedInEntity();
        String text = "";
        if (loggedIn.isPresent()) {
            if (loggedIn.get() instanceof ClientEntity) {
                ClientDTO loggedInClient = clientMapper.entityToDTO((ClientEntity) loggedIn.get());
                text = "Přihlášen klient: " + loggedInClient.getFirstName() + " " + loggedInClient.getLastName();
            }
        }
        model.addAttribute("loggedInUser", text);
    }
}
