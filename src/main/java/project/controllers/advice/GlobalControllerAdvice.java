package project.controllers.advice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import project.models.dtos.mappers.UserMapper;
import project.models.services.AuthenticationService;

/**
 * Slouží pro definování metod a atributů společných pro všechny controllery
 */
@ControllerAdvice
public class GlobalControllerAdvice {


    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserMapper userMapper;


    private String previousUrl;

    /**
     * Slouží k předání informace o přihlášeném uživateli do šablony, či fragmentu
     *
     * @param model
     */
    @ModelAttribute
    public void renderLoggedInUser(Model model) {
        String outputText = "";
        if (authenticationService.isUserLoggedIn()) {
            String user = authenticationService.getLoggedInUserName();
            String userRole = authenticationService.getLoggedInUserRole();
            outputText = "Přihlášen " + userRole + ": " + user;
        }
        model.addAttribute("loggedInUser", outputText);
    }


    public String getPreviousUrl() {
        return previousUrl;
    }

    public void setPreviousUrl(String previousUrl) {
        this.previousUrl = previousUrl;
    }
}
