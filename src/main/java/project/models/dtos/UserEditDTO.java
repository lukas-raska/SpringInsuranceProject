package project.models.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * Data transfer object (DTO) pro přenos dat z editačního formuláře klienta
 */
@Data
public class UserEditDTO {

    private long id;

    @NotBlank(message = "Vyplňte křestní jméno")
    private String firstName;

    @NotBlank(message = "Vyplňte příjmení")
    private String lastName;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent(message = "Pokud nejsi cestovatel v čase, datum narození musí být v minulosti :-)")
    private LocalDate dateOfBirth;
}
