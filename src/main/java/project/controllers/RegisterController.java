package project.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.models.dtos.ClientEditDTO;
import project.models.dtos.ClientRegistrationDTO;
import project.models.exceptions.DuplicateEmailException;
import project.models.exceptions.PasswordDoNotEqualException;
import project.models.services.ClientService;

@Controller
@RequestMapping("/register")
public class RegisterController {

    @Autowired
    private ClientService clientService;

    @GetMapping("/stepOne")
    public String renderRegisterStepOne(@ModelAttribute ClientRegistrationDTO registrationDTO) {
        return "pages/client/registerClient_stepOne";
    }

    @PostMapping("/stepOne")
    public String registerClientStepOne(
            @Valid @ModelAttribute ClientRegistrationDTO registrationDTO,
            BindingResult bindingResult
    ) {
        System.out.println("HomeController - registerClientStepOne - spuštěno");

        if (bindingResult.hasErrors()) {
            System.out.println("Chyby: " + bindingResult.toString());
            return renderRegisterStepOne(registrationDTO);
        }
        ClientEditDTO newClient = new ClientEditDTO();
        try {
             newClient = clientService.createNewClient(registrationDTO);
        } catch (DuplicateEmailException exception) {
            bindingResult.rejectValue("email", "error", "Zadaný e-mail je již používán");
            return renderRegisterStepOne(registrationDTO);
        } catch (PasswordDoNotEqualException exception) {
            bindingResult.rejectValue("password", "error", "Hesla se neshodují");
            bindingResult.rejectValue("confirmPassword", "error", "Hesla se neshodují");
            return renderRegisterStepOne(registrationDTO);
        }

                 return "redirect:/register/stepTwo/" + newClient.getId();
    }

    @GetMapping("/stepTwo/{clientId}")
    public String renderRegisterStepTwo(@ModelAttribute ClientEditDTO clientEditDTO) {
        return "pages/client/registerClient_stepTwo";
    }

    @PostMapping("/stepTwo/{clientId}")
    public String registerClientPartTwo(
            @PathVariable Long clientId,
            @Valid @ModelAttribute ClientEditDTO clientEditDTO,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ){
        if(bindingResult.hasErrors()){
            return renderRegisterStepTwo(clientEditDTO);
        }
        clientService.editClient(clientEditDTO);

        return "redirect:/register/stepThree/" + clientId;
    }



}
