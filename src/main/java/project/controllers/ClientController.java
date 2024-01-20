package project.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.data.entities.ClientEntity;
import project.models.dtos.ClientDTO;
import project.models.dtos.mappers.ClientMapper;
import project.models.exceptions.DuplicateEmailException;
import project.models.exceptions.PasswordDoNotEqualException;
import project.models.services.AuthenticationService;
import project.models.services.ClientService;

import java.util.Optional;

@Controller
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ClientMapper clientMapper;

    @GetMapping("/register")
    public String renderRegisterForm(@ModelAttribute ClientDTO dto) {
        return "pages/client/register";
    }

    @PostMapping("/register")
    public String registerNewClient(
            @Valid @ModelAttribute ClientDTO dto,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model
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

        redirectAttributes.addFlashAttribute("success", "Uživatel zaregistrován");


        return "redirect:/login";
    }


    @GetMapping("/detail")
    public String renderDetail(Model model
    ) {
        ClientDTO dto = new ClientDTO();

        Optional<ClientEntity> client = authenticationService.getLoggedInEntity();
        if (client.isPresent()) {
            dto = clientMapper.entityToDTO(client.get());
        }
        model.addAttribute("clientDTO", dto);

//
//        dto = clientService.loadUserByUsername()
//
//                clientMapper.entityToDTO()

        return "pages/client/detail";
    }

}
