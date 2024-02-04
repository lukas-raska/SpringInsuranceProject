package project.data.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import project.constant.InsuranceType;
import project.data.entities.ClientEntity;

import java.time.LocalDate;

@Entity(name = "insurance")
@Getter
@Setter
public class InsuranceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private ClientEntity insuredClient;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private InsuranceType insuranceType;

    @Column(nullable = false)
    private int amountOfInsurance;

    @Column(nullable = false)
    private LocalDate insuranceStart;

    @Column(nullable = false)
    private LocalDate insuranceEnd;

    @Column(nullable = false)
    private LocalDate dateOfRegistry;



}
