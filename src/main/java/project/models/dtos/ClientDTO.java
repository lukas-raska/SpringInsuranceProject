package project.models.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class ClientDTO {

    private long id;

    @NotBlank(message = "Vyplňte křestní jméno")
    private String firstName;

    @NotBlank(message = "Vyplňte příjmení")
    private String lastName;

    @NotBlank(message = "Vyplňte e-mail")
    @Email(message = "Vyplňte validní e-mail")
    private String email;

    @NotBlank(message = "Vyplňte heslo")
    @Size(min = 6, message = "Minimální délka hesla je 6 znaků")
    private String password;

    @NotBlank(message = "Vyplňte heslo")
    @Size(min = 6, message = "Minimální délka hesla je 6 znaků")
    private String confirmPassword;


    @NotBlank(message = "Vyplňte ulici")
    private String street;

    @NotBlank(message = "Vyplňte číslo domu")
    private String streetNumber;

    @NotBlank(message = "Vyplňte město")
    private String city;

    @NotBlank(message = "Vyplňte PSČ")
    private String zipCode;

    @NotBlank(message = "Vyplňte telefonní číslo.")
    private String phoneNumber;

    @NotNull(message = "Vyplňte datum narození")
    @DateTimeFormat(pattern = "dd.DD.yyyy")
    @PastOrPresent(message = "Pokud nejsi cestovatel v čase, datum narození musí být v minulosti :-)")
    private LocalDate dateOfBirth;



}
