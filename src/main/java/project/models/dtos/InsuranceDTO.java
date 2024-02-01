package project.models.dtos;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
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
    @FutureOrPresent(message = "Počátek pojištění nelze zadat v minulosti.")
    private LocalDate insuranceStart;

    @NotNull(message = "Zadejte konec pojištění")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent(message = "Počátek pojištění nelze zadat v minulosti.")
    private LocalDate insuranceEnd;


    private UserDisplayDTO insuredClient;

    private LocalDate dateOfRegistry;
}
