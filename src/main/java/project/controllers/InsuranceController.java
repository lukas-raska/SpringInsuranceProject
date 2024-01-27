package project.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.models.dtos.InsuranceDTO;
import project.models.exceptions.WrongInsuranceDatesException;
import project.models.services.InsuranceService;

import java.util.List;

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
            System.out.println("Chyby: " + result);
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

        return "redirect:/client/{clientId}";
    }

    @GetMapping("insurance/list")
    public String renderAllInsurances (Model model){
        List<InsuranceDTO> allInsurances = insuranceService.getAllInsurances();

        model.addAttribute("allInsurances", allInsurances);
        return "pages/insurance/list";
    }

}