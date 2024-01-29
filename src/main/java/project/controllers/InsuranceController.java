package project.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.models.dtos.InsuranceDTO;
import project.models.exceptions.InsuranceNotFoundException;
import project.models.exceptions.WrongInsuranceDatesException;
import project.models.services.InsuranceService;

import java.util.List;
import java.util.Optional;

@Controller

public class InsuranceController {

    @Autowired
    private InsuranceService insuranceService;

    @GetMapping("/client/{clientId}/insurance/new")
    public String renderNewInsuranceForm(
            //@PathVariable(name = "clientId") Long clientId,
            @ModelAttribute InsuranceDTO dto) {

        System.out.println("renderNewInsuranceForm spuštěno");
        return "pages/insurance/new";
    }

    @PostMapping("/client/{clientId}/insurance/new")
    public String createNewInsurance(
            @PathVariable(name = "clientId") Long clientId,
            @Valid @ModelAttribute InsuranceDTO dto,
            BindingResult result,
            RedirectAttributes redirectAttributes
    ) {
        if (result.hasErrors()) {
            return "pages/insurance/new";
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
        InsuranceDTO dto = insuranceService
                .getInsuranceById(insuranceId)
                .orElseThrow(InsuranceNotFoundException::new);

        model.addAttribute("insuranceDetail", dto);

        return "pages/insurance/detail";
    }

    @ExceptionHandler(InsuranceNotFoundException.class)
    public String handleInsuranceNotFoundException(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("insuranceNotFound", "Pojistná smlouva nenalezena.");
        return "redirect:/";
    }
}
