package project.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.models.dtos.InsuranceDTO;
import project.models.dtos.mappers.InsuranceMapper;
import project.models.exceptions.InsuranceNotFoundException;
import project.models.exceptions.WrongInsuranceDatesException;
import project.models.services.InsuranceService;

import java.util.List;

/**
 * Controller pro manipulaci s Insurance
 */
@Controller
public class InsuranceController {

    @Autowired
    private InsuranceService insuranceService;

    @Autowired
    private InsuranceMapper insuranceMapper;

    /**
     * Vykreslí formulář pro tvorbu nového pojištění
     * @param dto {@link InsuranceDTO}
     * @return Šablonu s formulářem
     */
    @GetMapping("/client/{clientId}/insurance/new")
    public String renderNewInsuranceForm(@ModelAttribute InsuranceDTO dto) {
        return "pages/insurance/new";
    }

    /**
     * Zpracuje požadavek na vytvoření nové pojistky
     * @param clientId - Identifikátor klienta (přijatý z URL)
     * @param dto - {@link InsuranceDTO} - objekt s daty vyplněnými ve formuláři
     * @param result - {@link BindingResult} - objekt pro práci s výsledky validace formuláře
     * @param redirectAttributes - slouží pro přidání flash zpráv po vytvoření
     * @return Po odeslání přesměruje na zvolenou adresu
     */
    @PostMapping("/client/{clientId}/insurance/new")
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
        try {
            dto.setClientId(clientId);
            insuranceService.newInsurance(dto);
        } catch (WrongInsuranceDatesException e) {
            result.rejectValue("insuranceStart", "error", "Konec pojištění nelze stanovit před jeho počátkem");
            result.rejectValue("insuranceEnd", "error", "Konec pojištění nelze stanovit před jeho počátkem");
            return "pages/insurance/new";
        }
        redirectAttributes.addFlashAttribute("newInsuranceCreated", "Pojistná smlouva vytvořena.");

        return "redirect:/client/{clientId}";
    }

    @GetMapping("insurance/list")
    public String renderAllInsurances(Model model) {
        List<InsuranceDTO> allInsurances = insuranceService.getAllInsurances();

        model.addAttribute("allInsurances", allInsurances);
        return "pages/insurance/list";
    }

    @GetMapping("insurance/{insuranceId}")
    public String renderInsuranceDetail(
            @PathVariable(name = "insuranceId") Long insuranceId,
            Model model
    ) {
        InsuranceDTO dto = insuranceService.getInsuranceById(insuranceId);

        model.addAttribute("insuranceDetail", dto);

        return "pages/insurance/detail";
    }

    @GetMapping("/insurance/{insuranceId}/edit")
    public String renderEditForm(
            @PathVariable(name = "insuranceId") Long insuranceId,
            @ModelAttribute InsuranceDTO dto
    ) {
        System.out.println("Metoda renderEditForm volána.");
        //předvyplnění formuláře - načtu data o pojistce z db a překopíruju do dto ve formuláři
        InsuranceDTO dtoToEdit = insuranceService.getInsuranceById(insuranceId);
        System.out.println("DTO to edit: " + dtoToEdit);
        System.out.println("dto formuláře před updatem: " + dto);
        insuranceMapper.updateInsuranceDTO(dtoToEdit, dto);
        System.out.println("dto formuláře po updatu: " + dto);
        return "pages/insurance/editForm";
    }

    @PostMapping("/insurance/{insuranceId}/edit")
    public String editInsurance(
            @PathVariable(name = "insuranceId") Long insuranceId,
            @Valid @ModelAttribute InsuranceDTO dto,
            BindingResult result,
            RedirectAttributes redirectAttributes
    ) {
        System.out.println("\nMetoda edit insurance volána.");
        if (result.hasErrors()) {
            System.out.println("Zjištěny vallidační chyby:");
            System.out.println(result.getAllErrors());
            return renderEditForm(insuranceId, dto);
        }
        System.out.println("DTO před editací: " + dto);
        dto.setId(insuranceId);
        insuranceService.editInsurance(dto);
        System.out.println("DTO po editaci: " + dto);

        redirectAttributes.addFlashAttribute("successInsuranceEdit", "Údaje změněny");

        return "redirect:/insurance/list";
    }

    @ExceptionHandler(InsuranceNotFoundException.class)
    public String handleInsuranceNotFoundException(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("insuranceNotFound", "Pojistná smlouva nenalezena.");
        return "redirect:/insurance/list";
    }
}
