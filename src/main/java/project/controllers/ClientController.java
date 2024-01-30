package project.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.data.entities.ClientEntity;
import project.models.dtos.ClientDisplayDTO;
import project.models.dtos.ClientEditDTO;
import project.models.dtos.client.ClientRegisterDTO;
import project.models.dtos.mappers.ClientMapper;
import project.models.exceptions.ClientNotFoundException;
import project.models.exceptions.DuplicateEmailException;
import project.models.exceptions.InsuranceNotFoundException;
import project.models.exceptions.PasswordDoNotEqualException;
import project.models.services.AuthenticationService;
import project.models.services.ClientService;
import project.models.services.InsuranceService;

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
    private ClientMapper clientMapper;

    /**
     * Zobrazí formulář pro registraci nového klienta
     *
     * @param dto (Objekt ClientRegisterDTO pro předvyplnění formuláře (pokud existuje)
     * @return Název šablony pro zobrazení formuláře
     */
    @GetMapping("/register")
    public String renderRegisterForm(@ModelAttribute ClientRegisterDTO dto) {
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
            @Valid @ModelAttribute ClientRegisterDTO dto,
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
    public String renderLoggedClientDetail(Model model
    ) {
        ClientDisplayDTO dto = new ClientDisplayDTO();

        //získání dat o přihlášeném klientoví a převedení na DTO
        Optional<ClientEntity> client = authenticationService.getLoggedInEntity();
        if (client.isPresent()) {
            dto = clientMapper.entityToDTO(client.get());
        }
        //předání do šablony
        model.addAttribute("clientDTO", dto);

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
    public String renderCustomClientDetail(
            @PathVariable(name = "clientId") Long clientId,
            Model model
    ) {
        model.addAttribute("clientDTO", clientService.getClientById(clientId));
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
    public String renderAllClientsList(Model model) {
        model.addAttribute("allClientsList", clientService.getAllClients());
        return "pages/client/list";
    }

    @GetMapping("/edit/{clientId}")
    public String renderEditForm(
            @PathVariable(name = "clientId") Long clientId,
            ClientEditDTO dto

    ) {
        //načtu data klienta k předvyplnění formuláře
        ClientDisplayDTO fetchedClient = clientService.getClientById(clientId);

        //načtenými daty updatuju dto předávané metodou controlleru
        clientMapper.updateClientDTO(fetchedClient, dto);


        return "pages/client/edit";
    }


    @PostMapping("/edit/{clientId}")
    public String editClient(
            @PathVariable(name = "clientId") Long clientId,
            @Valid @ModelAttribute ClientEditDTO dto,
            BindingResult result,
            RedirectAttributes redirectAttributes
    ) {
        //kontrola validace - v případě chyb opětovné vykreslení formuláře + chybových hlášek (viz šablona)
        if (result.hasErrors()) {
            System.out.println("Chyby: " + result);
            return "pages/client/edit";
        }

        dto.setId(clientId);
        clientService.editClient(dto);

        redirectAttributes.addFlashAttribute("successEdit", "Úprava údajů proběhla úspěšně.");

        return "redirect:/";
    }

    @PostMapping("/delete/{clientId}")
    public String deleteClient(
            @PathVariable(name = "clientId") Long clientId,
            RedirectAttributes redirectAttributes
    ){
        clientService.deleteClient(clientId);
        redirectAttributes.addFlashAttribute("successDelete", "Klient odstraněn.");

        return "redirect:/";
    }

    @ExceptionHandler(ClientNotFoundException.class)
    public String handleClientNotFoundException(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("clientNotFound", "Klient nenalezen");
        return "redirect:/client/list";
    }

}
