package project.controllers.advice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import project.data.entities.ClientEntity;
import project.models.dtos.ClientDisplayDTO;
import project.models.dtos.ClientRegisterDTO;
import project.models.dtos.mappers.ClientMapper;
import project.models.services.AuthenticationService;

import java.util.Optional;

/**
 * Globální ControllerAdvice třída pro celou aplikaci
 */
@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ClientMapper clientMapper;

    /**
     * Slouží k předání informace o přihlášeném uživateli do šablony, či fragmentu
     * @param model
     */
    @ModelAttribute
    public void loggedInUser(Model model) {

        //získání detailů o přihlášeném uživateli
        Optional<UserDetails> loggedIn = authenticationService.getLoggedInEntity();
        String text = "";

        //pokud je uživatel přihlášen
        if (loggedIn.isPresent()) {
            //pokud je přihlášen klient
            if (loggedIn.get() instanceof ClientEntity) {
                //převedu na příslušné DTO
                ClientDisplayDTO loggedInClient = clientMapper.entityToDTO((ClientEntity) loggedIn.get());
                //upravím text pro předání do šablony
                text = "Přihlášen klient: " + loggedInClient.getFirstName() + " " + loggedInClient.getLastName();
            }
        }
        //předání do šablony
        model.addAttribute("loggedInUser", text);
    }
}
