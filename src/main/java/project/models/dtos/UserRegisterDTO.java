package project.models.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class UserRegisterDTO {

    @NotBlank(message = "Vyplňte křestní jméno")
    private String firstName;

    @NotBlank(message = "Vyplňte příjmení")
    private String lastName;

    @NotBlank(message = "Vyplňte e-mail")
    @Email(message = "Vyplňte validní e-mail")
    private String email;

    @NotBlank(message = "Vyplňte telefonní číslo.")
    private String phoneNumber;

    @NotBlank(message = "Vyplňte heslo")
    @Size(min = 6, message = "Minimální délka hesla je 6 znaků")
    private String password;

    @NotBlank(message = "Vyplňte heslo")
    @Size(min = 6, message = "Minimální délka hesla je 6 znaků")
    private String confirmPassword;

    @NotNull(message = "Vyplňte datum narození")
    @DateTimeFormat(pattern = "dd.DD.yyyy")
    @PastOrPresent(message = "Pokud nejsi cestovatel v čase, datum narození musí být v minulosti :-)")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Vyplňte název ulice")
    private String street;

    @NotBlank(message = "Vyplňte číslo domu")
    private String streetNumber;

    @NotBlank(message = "Vyplňte název města")
    private String city;

    @NotBlank(message = "Vyplňte PSČ")
    private String zipCode;
}

