package project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import project.models.dtos.ClientDTO;
import project.models.services.ClientService;

/**
 * Controller pro základní navigaci
 */
@Controller
public class HomeController {

    @Autowired
    private ClientService clientService;

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
}
