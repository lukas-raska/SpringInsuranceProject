package project.models.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;


import java.time.LocalDate;

@Data
public class ClientEditDTO {

    private long id;

    private String firstName;

    private String lastName;

    private String email;

    @NotBlank(message = "Vyplňte ulici a číslo popisné")
    private String street;

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



