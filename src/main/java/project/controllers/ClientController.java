package project.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.models.dtos.ClientDTO;
import project.models.exceptions.DuplicateEmailException;
import project.models.exceptions.PasswordDoNotEqualException;
import project.models.services.ClientService;

@Controller
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @GetMapping("/register")
    public String renderRegisterForm(@ModelAttribute ClientDTO dto) {
        return "pages/client/register";
    }

    @PostMapping("/register")
    public String registerNewClient(
            @Valid @ModelAttribute ClientDTO dto,
            BindingResult result,
            RedirectAttributes redirectAttributes
            ) {
        if (result.hasErrors()) {
            return renderRegisterForm(dto);
        }

        try {
            clientService.createNewClient(dto);
        } catch (DuplicateEmailException exception) {
            result.rejectValue("email", "error", "Zadaný e-mail je již používán");
            return renderRegisterForm(dto);
        } catch (PasswordDoNotEqualException exception) {
            result.rejectValue("password", "error", "Hesla se neshodují");
            result.rejectValue("confirmPassword", "error", "Hesla se neshodují");
            return renderRegisterForm(dto);
        }

        return "redirect:/login";
    }

}
