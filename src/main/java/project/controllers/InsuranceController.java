package project.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.data.entities.ClientEntity;
import project.models.dtos.InsuranceDTO;
import project.models.dtos.mappers.InsuranceMapper;
import project.models.exceptions.InsuranceNotFoundException;
import project.models.exceptions.WrongInsuranceDatesException;
import project.models.services.AuthenticationService;
import project.models.services.ClientService;
import project.models.services.InsuranceService;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

/**
 * Controller pro manipulaci s Insurance
 */
@Controller
public class InsuranceController {

    @Autowired
    private InsuranceService insuranceService;

    @Autowired
    private InsuranceMapper insuranceMapper;

    @Autowired
    private ClientService clientService;

    @Autowired
    private AuthenticationService authenticationService;

    /**
     * Vykreslí formulář pro tvorbu nového pojištění
     *
     * @param dto {@link InsuranceDTO}
     * @return Šablonu s formulářem
     */
    @GetMapping("/client/{clientId}/insurance/new")
    @Secured({"ROLE_ADMIN", "ROLE_EMPLOYEE", "ROLE_CLIENT"})
    public String renderNewInsuranceForm(@ModelAttribute InsuranceDTO dto) {
        return "pages/insurance/new";
    }

    /**
     * Zpracuje požadavek na vytvoření nové pojistky
     *
     * @param clientId           - Identifikátor klienta (přijatý z URL)
     * @param dto                - {@link InsuranceDTO} - objekt s daty vyplněnými ve formuláři
     * @param result             - {@link BindingResult} - objekt pro práci s výsledky validace formuláře
     * @param redirectAttributes - slouží pro přidání flash zpráv po vytvoření
     * @return Po odeslání přesměruje na zvolenou adresu
     */
    @PostMapping("/client/{clientId}/insurance/new")
    @Secured({"ROLE_ADMIN", "ROLE_EMPLOYEE", "ROLE_CLIENT"})
    public String createNewInsurance(
            @PathVariable(name = "clientId") Long clientId,
            @Valid @ModelAttribute InsuranceDTO dto,
            BindingResult result,
            RedirectAttributes redirectAttributes
    ) {
        //v případě validačních chyb opětovně vykreslí formulář
        if (result.hasErrors()) {
            return renderNewInsuranceForm(dto);
        }
        //pokud o uložení do databáze
        try {
            insuranceService.newInsurance(dto);
            //zachycení výjimky v případě chybně zadaného data
        } catch (WrongInsuranceDatesException e) {
            result.rejectValue("insuranceStart", "error", "Konec pojištění nelze stanovit před jeho počátkem");
            result.rejectValue("insuranceEnd", "error", "Konec pojištění nelze stanovit před jeho počátkem");
            return renderNewInsuranceForm(dto);
        }
        //flash zpráva při přesměrování
        redirectAttributes.addFlashAttribute("newInsuranceCreated", "Pojistná smlouva vytvořena.");

        //pokud je klient přihlášen, přesměruji na jeho osobní stránku
        Optional<ClientEntity> loggedInClient = authenticationService.getLoggedInEntity();
        if (loggedInClient.isPresent() && loggedInClient.get().getId().equals(clientId)) {
            return "redirect:/client/myDetail";
        }
        //jinak do detailu klienta dle ID
        return "redirect:/client/{clientId}";
    }

    @GetMapping("insurance/list")
    @Secured({"ROLE_ADMIN", "ROLE_EMPLOYEE"})
    public String renderAllInsurances(Model model) {
        List<InsuranceDTO> allInsurances = insuranceService.getAllInsurances();

        model.addAttribute("allInsurances", allInsurances);
        return "pages/insurance/list";
    }

    @GetMapping("insurance/{insuranceId}")
    @Secured({"ROLE_ADMIN", "ROLE_EMPLOYEE", "ROLE_CLIENT"})
    public String renderInsuranceDetail(
            @PathVariable(name = "insuranceId") Long insuranceId,
            Model model
    ) {
        InsuranceDTO dto = insuranceService.getInsuranceById(insuranceId);

        model.addAttribute("insuranceDetail", dto);

        return "pages/insurance/detail";
    }

    @GetMapping("/insurance/{insuranceId}/edit")
    @Secured({"ROLE_ADMIN", "ROLE_EMPLOYEE"})
    public String renderEditForm(
            @PathVariable(name = "insuranceId") Long insuranceId,
            @ModelAttribute InsuranceDTO dto
    ) {
        //předvyplnění formuláře - načtu data o pojistce z db a překopíruju do dto ve formuláři
        InsuranceDTO dtoToEdit = insuranceService.getInsuranceById(insuranceId);
        insuranceMapper.updateInsuranceDTO(dtoToEdit, dto);

        return "pages/insurance/editForm";
    }

    @PostMapping("/insurance/{insuranceId}/edit")
    @Secured({"ROLE_ADMIN", "ROLE_EMPLOYEE"})
    public String editInsurance(
            @PathVariable(name = "insuranceId") Long insuranceId,
            @Valid @ModelAttribute InsuranceDTO dto,
            BindingResult result,
            RedirectAttributes redirectAttributes
    ) {

        if (result.hasErrors()) {
            return renderEditForm(insuranceId, dto);
        }

        dto.setId(insuranceId);
        insuranceService.editInsurance(dto);


        redirectAttributes.addFlashAttribute("successInsuranceEdit", "Údaje změněny");

        return "redirect:/insurance/list";
    }

    @ExceptionHandler(InsuranceNotFoundException.class)
    public String handleInsuranceNotFoundException(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("insuranceNotFound", "Pojistná smlouva nenalezena.");
        return "redirect:/insurance/list";
    }
}
