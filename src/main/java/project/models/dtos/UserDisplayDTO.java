package project.models.dtos;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDisplayDTO {

    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String street;
    private String streetNumber;
    private String city;
    private String zipCode;
    private String phoneNumber;
    private LocalDate dateOfBirth;
    private int age;
    private LocalDate dateOfRegistration;
}
