package project.models.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ClientRegistrationDTO {

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


}
