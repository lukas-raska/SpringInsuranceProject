package project.controllers;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import project.data.entities.ClientEntity;
import project.models.services.AuthenticationService;
import project.models.services.ClientService;

import java.util.Optional;

/**
 * Controller pro základní navigaci
 */
@Controller
public class HomeController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private AuthenticationService authenticationService;

    /**
     * Zobrazí úvodní stránku
     *
     * @return Název šablony pro zobrazení úvodní stránky
     */
    @GetMapping
    public String renderIndex() {
        return "pages/home/index";
    }

    /**
     * Zobrazí sekci "o aplikaci"
     * @return Název šablony pro zobrazení sekce "o aplikaci"
     */
    @GetMapping("/about")
    public String renderAbout() {
        return "pages/home/about";
    }

    /**
     * Zobrazí formulář pro login
     * @return Název šablony s login formulářem
     */
    @GetMapping("/login")
    public String renderLogin() {
        return "pages/home/login";
    }

    /**
     * Slouží k nastavení přesměrování po přihlášení v závislosti na roli přihlášené entity
     * @param request {@link HttpServletRequest}
     * @return - přesměrování na zvolenou URL
     */
    @GetMapping("/afterLogin")
    public String redirectAfterLogin(HttpServletRequest request){
        if(request.isUserInRole("ROLE_CLIENT")){
            return "redirect:/client/myDetail";
        }
        if(request.isUserInRole("ROLE_EMPLOYEE")){
            return "redirect:/client/list";
        }
        if(request.isUserInRole("ROLE_ADMIN")){
            return "redirect:/employee/list";
        }
        return "redirect:/";



    }
}
