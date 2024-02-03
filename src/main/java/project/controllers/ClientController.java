package project.controllers;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.data.entities.ClientEntity;
import project.models.dtos.InsuranceDTO;
import project.models.dtos.UserDisplayDTO;
import project.models.dtos.UserEditDTO;
import project.models.dtos.UserRegisterDTO;
import project.models.dtos.mappers.UserMapper;
import project.models.exceptions.ClientNotFoundException;
import project.models.exceptions.DuplicateEmailException;
import project.models.exceptions.PasswordDoNotEqualException;
import project.models.services.AuthenticationService;
import project.models.services.ClientService;
import project.models.services.InsuranceService;

import java.util.List;
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
    private InsuranceService insuranceService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserMapper userMapper;

    /**
     * Zobrazí formulář pro registraci nového klienta
     *
     * @param dto (Objekt ClientRegisterDTO pro předvyplnění formuláře (pokud existuje)
     * @return Název šablony pro zobrazení formuláře
     */
    @GetMapping("/register")
    public String renderRegisterForm(@ModelAttribute UserRegisterDTO dto) {
        return "pages/client/register";
    }


    /**
     * Zpracuje požadavek na registraci nového klienta
     *
     * @param dto                Objekt ClientRegisterDTO obsahující informace o klientovi vyplněné ve formuláři
     * @param result             objekt BindingResult pro práci s výsledky validace formuláře
     * @param redirectAttributes Atributy pro přesměrování (slouží pro přidávání flash zpráv)
     * @param model              Model pro předávání dat do šablony
     * @return Vrací název šablony pro zobrazení stránky po registraci
     */
    @PostMapping("/register")
    public String registerNewClient(
            @Valid @ModelAttribute UserRegisterDTO dto,
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
            clientService.create(dto);

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
        redirectAttributes.addFlashAttribute("successRegister", "Uživatel zaregistrován");

        return "redirect:/login";
    }

    /**
     * Zobrazí detail přihlášeného klienta
     *
     * @param model Model pro předání dat do šablony
     * @return Název šablony pro zobrazení stránky s detailem
     */
    @GetMapping("/myDetail")
    @Secured("ROLE_CLIENT")
    public String renderLoggedInClientDetail(Model model) {

        UserDisplayDTO clientDetail = new UserDisplayDTO();

        //získání dat o přihlášeném klientoví a převedení na DTO
        Optional<UserDetails> user = authenticationService.getLoggedInEntity();
        if (user.isPresent() && user.get() instanceof ClientEntity) {
            clientDetail = userMapper.mapToDTO((ClientEntity) user.get());
        }
        //získání všech pojistek klienta
        List<InsuranceDTO> clientsInsurances = insuranceService.getInsurancesByClientId(clientDetail.getId());

        //předání do šablony
        model.addAttribute("clientDTO", clientDetail);
        model.addAttribute("clientInsurances", clientsInsurances);

        return "pages/client/detail";
    }

    /**
     * Zobrazí detail klienta na základě jeho ID
     *
     * @param clientId Identifikátor klienta (získáván z URL)
     * @param model    Model pro předání dat do šablony
     * @return Šablonu pro zobrazení stránky
     */
    @GetMapping("{clientId}")
    @Secured({"ROLE_EMPLOYEE", "ROLE_ADMIN"})
    public String renderCustomClientDetail(
            @PathVariable(name = "clientId") Long clientId,
            Model model
    ) {
        model.addAttribute("clientDTO", clientService.getById(clientId));
        model.addAttribute("clientInsurances", insuranceService.getInsurancesByClientId(clientId));

        return "pages/client/detail";
    }

    /**
     * Slouží k zobrazení seznamu všech klientů
     *
     * @param model Model pro předání dat do šablony
     * @return Šablonu pro zobrazení stránky
     */
    @GetMapping("/list")
    @Secured({"ROLE_EMPLOYEE", "ROLE_ADMIN"})
    public String renderAllClientsList(Model model) {
        model.addAttribute("allClientsList", clientService.getAll());
        return "pages/client/list";
    }

    /**
     * Zobrazí formulář pro editaci klienta
     *
     * @param clientId - identifikátor klienta
     * @param dto      - {@link UserEditDTO}
     * @return - šablonu s formulářem
     */
    @GetMapping("/edit/{clientId}")
    @Secured({"ROLE_EMPLOYEE", "ROLE_ADMIN", "ROLE_CLIENT"})
    public String renderEditForm(
            @PathVariable(name = "clientId") Long clientId,
            UserEditDTO dto
    ) {
        //načtu data klienta k předvyplnění formuláře
        UserDisplayDTO fetchedClient = clientService.getById(clientId);

        //načtenými daty updatuju dto předávané metodou controlleru
        userMapper.updateUserDTO(fetchedClient, dto);

        return "pages/client/edit";
    }

    /**
     * Zpracuje požadavek na editaci klienta
     *
     * @param clientId           - identifikátor klienta
     * @param dto                - data z formuláře {@link UserEditDTO}
     * @param result             - objekt {@link BindingResult} pro zachytávání validačních chyb
     * @param redirectAttributes - pro vytváření flash zpráv při přesměrování
     * @return - přesměrování na požadvanou adresu
     */
    @PostMapping("/edit/{clientId}")
    @Secured({"ROLE_EMPLOYEE", "ROLE_ADMIN", "ROLE_CLIENT"})
    public String editClient(
            @PathVariable(name = "clientId") Long clientId,
            @Valid @ModelAttribute UserEditDTO dto,
            BindingResult result,
            RedirectAttributes redirectAttributes
    ) {
        //kontrola validace - v případě chyb opětovné vykreslení formuláře + chybových hlášek (viz šablona)
        if (result.hasErrors()) {
            return renderEditForm(clientId, dto);
        }

        dto.setId(clientId); //nutné nastavit hodnotou z URL
        clientService.edit(dto);

        redirectAttributes.addFlashAttribute("successEdit", "Úprava údajů proběhla úspěšně.");

        return "redirect:/";
    }

    /**
     * Zpracuje požadavek na mazání klienta
     * @param clientId - identifikátor klienta
     * @param redirectAttributes - pro vytváření flash zpráv při přesměrování
     * @return - přesměrování na požadovanou URL
     */
    @PostMapping("/delete/{clientId}")
    @Secured({"ROLE_EMPLOYEE", "ROLE_ADMIN", "ROLE_CLIENT"})
    public String deleteClient(
            @PathVariable(name = "clientId") Long clientId,
            RedirectAttributes redirectAttributes
    ) {
        clientService.remove(clientId);

        redirectAttributes.addFlashAttribute("successDelete", "Klient odstraněn.");

        return "redirect:/";
    }

    @ExceptionHandler(ClientNotFoundException.class)
    public String handleClientNotFoundException(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("clientNotFound", "Klient nenalezen");
        return "redirect:/client/list";
    }

}
