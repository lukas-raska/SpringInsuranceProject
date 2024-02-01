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
import project.models.dtos.UserRegisterDTO;
import project.models.exceptions.DuplicateEmailException;
import project.models.exceptions.PasswordDoNotEqualException;
import project.models.services.EmployeeService;

@Controller
@RequestMapping(value = "/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/register")
    public String renderRegisterForm(@ModelAttribute UserRegisterDTO dto){
        return "pages/employee/register";
    }

    @PostMapping("/register")
    public String registerNewEmployee(
            @Valid @ModelAttribute UserRegisterDTO dto,
            BindingResult result,
            RedirectAttributes redirectAttributes
    ){
        if(result.hasErrors()){
            return renderRegisterForm(dto);
        }
        try{
            employeeService.create(dto);
        }catch (DuplicateEmailException exception){
            result.rejectValue("email", "error", "Zadaný email je již používán" );
            return renderRegisterForm(dto);
        }catch (PasswordDoNotEqualException exception){
            result.rejectValue("password", "error", "Hesla se neshodují");
            result.rejectValue("confirmPassword", "error", "Hesla se neshodují");
            return renderRegisterForm(dto);
        }
        return "redirect:/login";
    }
}

