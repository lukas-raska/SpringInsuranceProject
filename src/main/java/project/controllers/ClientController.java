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

/**
 * Controller pro manipulaci s klienty
 */
@Controller
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private ClientMapper clientMapper;

    /**
     * Zobrazí formulář pro registraci nového klienta
     *
     * @param dto (Objekt ClientDTO pro předvyplnění formuláře (pokud existuje)
     * @return Název šablony pro zobrazení formuláře
     */
    @GetMapping("/register")
    public String renderRegisterForm(@ModelAttribute ClientDTO dto) {
        return "pages/client/register";
    }


    /**
     * Zpracuje požadavek na registraci nového klienta
     *
     * @param dto                Objekt ClientDTO obsahující informace o klientovi vyplněné ve formuláři
     * @param result             objekt BindingResult pro práci s výsledky validace formuláře
     * @param redirectAttributes Atributy pro přesměrování (slouží pro přidávání flash zpráv)
     * @param model              Model pro předávání dat do šablony
     * @return Vrací název šablony pro zobrazení stránky po registraci
     */
    @PostMapping("/register")
    public String registerNewClient(
            @Valid @ModelAttribute ClientDTO dto,
            BindingResult result,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        //pokud existují validační chyby, vykreslí se opětovně formulář s validačními hláškami
        if (result.hasErrors()) {
            return renderRegisterForm(dto);
        }

        try {
            //pokusí se vytvořit nového klienta
            clientService.createNewClient(dto);

            //ošetření chyby duplicity emailu
        } catch (DuplicateEmailException exception) {
            result.rejectValue("email", "error", "Zadaný e-mail je již používán");
            return renderRegisterForm(dto);

            //ošetření chyby nestejného hesla
        } catch (PasswordDoNotEqualException exception) {
            result.rejectValue("password", "error", "Hesla se neshodují");
            result.rejectValue("confirmPassword", "error", "Hesla se neshodují");
            return renderRegisterForm(dto);
        }

        //po úspěšné registraci přesměrování na přihlášení a zobrazení flash zprávy
        redirectAttributes.addFlashAttribute("success", "Uživatel zaregistrován");
        return "redirect:/login";
    }

    /**
     * Zobrazí detail přihlášeného klienta
     * @param model Model pro předání dat do šablony
     * @return Název šablony pro zobrazení stránky s detailem
     */
    @GetMapping("/detail")
    public String renderLoggedClientDetail(Model model
    ) {
        ClientDTO dto = new ClientDTO();

        //získání dat o přihlášeném klientoví a převedení na DTO
        Optional<ClientEntity> client = authenticationService.getLoggedInEntity();
        if (client.isPresent()) {
            dto = clientMapper.entityToDTO(client.get());
        }
        //předání do šablony
        model.addAttribute("clientDTO", dto);

        return "pages/client/detail";
    }

}
