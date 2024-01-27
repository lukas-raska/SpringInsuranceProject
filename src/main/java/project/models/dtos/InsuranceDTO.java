package project.models.dtos;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import project.data.entities.ClientEntity;
import project.data.entities.insurance.InsuranceType;

import java.time.LocalDate;

@Data
public class InsuranceDTO {

    private long id;

    @NotNull
    private long clientId;

    @NotNull(message = "Zadejte typ pojištění")
    private InsuranceType insuranceType;

    @NotNull(message = "Zadejte pojistnou částku")
    @PositiveOrZero(message = "Nelze zadat záporné číslo")
    private int amountOfInsurance;

    @NotNull(message = "Zadejte počátek pojištění")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate insuranceStart;

    @NotNull(message = "Zadejte konec pojištění")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate insuranceEnd;


    private ClientDisplayDTO insuredClient;

    private LocalDate dateOfRegistry;
}
