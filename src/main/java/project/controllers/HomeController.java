package project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import project.models.dtos.ClientDTO;
import project.models.services.ClientService;

@Controller
public class HomeController {

    @Autowired
    private ClientService clientService;

    @GetMapping
    public String renderIndex(Model model){
                return "pages/home/index";
    }

    @GetMapping({"/about", "/about/"})
    public String renderAbout(){
        return "pages/home/about";
    }

    @GetMapping("/login")
    public String renderLogin(){
        return "pages/home/login";
    }
}
