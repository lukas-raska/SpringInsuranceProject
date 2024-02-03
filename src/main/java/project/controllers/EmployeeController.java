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
import project.data.entities.EmployeeEntity;
import project.models.dtos.UserDisplayDTO;
import project.models.dtos.UserEditDTO;
import project.models.dtos.UserRegisterDTO;
import project.models.dtos.mappers.UserMapper;
import project.models.exceptions.ClientNotFoundException;
import project.models.exceptions.DuplicateEmailException;
import project.models.exceptions.EmployeeNotFoundException;
import project.models.exceptions.PasswordDoNotEqualException;
import project.models.services.AuthenticationService;
import project.models.services.ClientService;
import project.models.services.EmployeeService;

import java.util.List;
import java.util.Optional;

/**
 * Controller pro správu požadavků týkajících se zaměstanence
 */
@Controller
@RequestMapping(value = "/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserMapper userMapper;


    /**
     * Vykreslí formulář pro registraci zaměstnance
     *
     * @param dto - {@link UserRegisterDTO}
     * @return - vrací cestu k šabloně s formulářem
     */
    @GetMapping("/register")
    @Secured("ROLE_ADMIN")
    public String renderRegisterForm(@ModelAttribute UserRegisterDTO dto) {
        return "pages/employee/register";
    }

    /**
     * Zpracuje požadavek na registraci zaměstnance
     *
     * @param dto                - {@link UserRegisterDTO}
     * @param result             - objekt {@link BindingResult} pro správu validačních chyb
     * @param redirectAttributes - pro vytváření flash zpráv při přesměrování
     * @return - přesměrování na požadovanou URL
     */
    @PostMapping("/register")
    @Secured("ROLE_ADMIN")
    public String registerNewEmployee(
            @Valid @ModelAttribute UserRegisterDTO dto,
            BindingResult result,
            RedirectAttributes redirectAttributes
    ) {
        //v případě validačních chyb vykreslí znovu formulář
        if (result.hasErrors()) {
            return renderRegisterForm(dto);
        }
        //pokus o vytvoření zaměstance
        try {
            employeeService.create(dto);

            //zachytává chybu při zadání duplicitního mailu
        } catch (DuplicateEmailException exception) {
            result.rejectValue("email", "error", "Zadaný email je již používán");
            return renderRegisterForm(dto);

            //zachtává chybu při chybně vyplněném heslu
        } catch (PasswordDoNotEqualException exception) {
            result.rejectValue("password", "error", "Hesla se neshodují");
            result.rejectValue("confirmPassword", "error", "Hesla se neshodují");
            return renderRegisterForm(dto);
        }
        return "redirect:/login";
    }

    /**
     * Vykresluje seznam všech zaměstnanců
     *
     * @param model - objekt pro předání dat do šablony
     * @return - vrací cestu k šabloně
     */
    @GetMapping("/list")
    @Secured("ROLE_ADMIN")
    public String renderAllEmployeesList(Model model) {
        //načtení údajů z databáze
        List<UserDisplayDTO> allEmployees = employeeService.getAll();
        //předání dat šabloně
        model.addAttribute("allEmployeesList", allEmployees);
        //vykreslení šablony
        return "pages/employee/list";
    }

    /**
     * Vykreslí detail přihlášeného zaměstnance
     *
     * @param model - slouží pro předání dat do šablony
     * @return - cestu k šabloně
     */
    @GetMapping("/myDetail")
    @Secured({"ROLE_EMPLOYEE", "ROLE_ADMIN"})
    public String renderLoggedInEmployee(Model model) {

        UserDisplayDTO employeeDetail = new UserDisplayDTO();

        //získání dat o přihlášeném uživateli (zaměstanci)
        Optional<UserDetails> loggedInUser = authenticationService.getLoggedInEntity();
        if (loggedInUser.isPresent() && loggedInUser.get() instanceof EmployeeEntity) {
            employeeDetail = userMapper.mapToDTO((EmployeeEntity) loggedInUser.get());
        }
        //předání dat do šablony
        model.addAttribute("employeeDetail", employeeDetail);

        return "pages/employee/detail";
    }

    /**
     * Zobrazí detail zaměstnance na základě jeho ID
     *
     * @param employeeId Identifikátor klienta (získáván z URL)
     * @param model    Model pro předání dat do šablony
     * @return Šablonu pro zobrazení stránky
     */
    @GetMapping("{employeeId}")
    @Secured("ROLE_ADMIN")
    public String renderCustomEmployeeDetail(
            @PathVariable(name = "employeeId") Long employeeId,
            Model model
    ) {
        UserDisplayDTO employeeDetail = employeeService.getById(employeeId);
        model.addAttribute("employeeDetail", employeeDetail );

        return "pages/employee/detail";
    }

    @GetMapping("/edit/{employeeId}")
    @Secured("ROLE_ADMIN")
    public String renderEditForm(
            @PathVariable(name = "employeeId") Long employeeId,
            UserEditDTO dto
    ) {
        //načtu data klienta k předvyplnění formuláře
        UserDisplayDTO fetchedEmployee = employeeService.getById(employeeId);

        //načtenými daty updatuju dto předávané metodou controlleru
        userMapper.updateUserDTO(fetchedEmployee, dto);

        return "pages/employee/edit";
    }

    @PostMapping("/edit/{employeeId}")
    @Secured("ROLE_ADMIN")
    public String editEmployee(
            @PathVariable(name = "employeeId") Long employeeId,
            @Valid @ModelAttribute UserEditDTO dto,
            BindingResult result,
            RedirectAttributes redirectAttributes
    ) {
        //kontrola validace - v případě chyb opětovné vykreslení formuláře + chybových hlášek (viz šablona)
        if (result.hasErrors()) {
            return renderEditForm(employeeId, dto);
        }

        dto.setId(employeeId); //nutné nastavit hodnotou z URL
        employeeService.edit(dto);

        redirectAttributes.addFlashAttribute("successEdit", "Úprava údajů proběhla úspěšně.");

        return "redirect:/";
    }

    @PostMapping("/delete/{employeeId}")
    @Secured("ROLE_ADMIN")
    public String deleteEmployee(
            @PathVariable(name = "employeeId") Long employeeId,
            RedirectAttributes redirectAttributes
    ) {
        employeeService.remove(employeeId);

        redirectAttributes.addFlashAttribute("successDelete", "Zaměstnanec odstraněn.");

        return "redirect:/";
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    public String handleEmployeeNotFoundException(RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("employeeNotFound", "Zaměstnanec nenalezen");
        return "redirect:/employee/list";
    }

}

