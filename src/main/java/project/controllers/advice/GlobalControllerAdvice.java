package project.controllers.advice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import project.models.dtos.mappers.UserMapper;
import project.models.services.AuthenticationService;

/**
 * Globální ControllerAdvice třída pro celou aplikaci
 */
@ControllerAdvice
public class GlobalControllerAdvice {


    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserMapper userMapper;

    /**
     * Slouží k předání informace o přihlášeném uživateli do šablony, či fragmentu
     *
     * @param model
     */
//    @ModelAttribute
//    public void renderloggedInUser(Model model) {
//
//        //získání detailů o přihlášeném uživateli
//        Optional<UserDetails> loggedIn = authenticationService.getLoggedInEntity();
//        UserRole entityName = "";
//        UserRole renderedText = "";
//        UserDisplayDTO loggedInUser = new UserDisplayDTO();
//        //pokud je uživatel přihlášen
//        if (loggedIn.isPresent()) {
//            //pokud je přihlášeným klient
//            if (loggedIn.get() instanceof ClientEntity) {
//                //převedu na příslušné DTO
//                loggedInUser = userMapper.mapToDTO((ClientEntity) loggedIn.get());
//                entityName = "klient";
//
//            }
//            //pokud je přihlášeným zaměstnanec
//            if (loggedIn.get() instanceof EmployeeEntity) {
//                loggedInUser = userMapper.mapToDTO((EmployeeEntity) loggedIn.get());
//                entityName = "zaměstnanec";
//            }
//            //sestavení vypisovaného textu
//            renderedText = "Přihlášen " + entityName + ": " + loggedInUser.getFirstName() + " " + loggedInUser.getLastName();
//        }
//        //předání nápisu šabloně
//        model.addAttribute("loggedInUser", renderedText);
//    }
    @ModelAttribute
    public void renderLoggedInUser(Model model) {
        String outputText = "";

        if (authenticationService.isUserLoggedIn()) {

            String user = authenticationService.getLoggedInUserName();
            String userRole = authenticationService.getLoggedInUserRole();
            outputText = "Přihlášen " + userRole + ": " + user;
        }
        model.addAttribute("loggedInUser", outputText);

    }


}
