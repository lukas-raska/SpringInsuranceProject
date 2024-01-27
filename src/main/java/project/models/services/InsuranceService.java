package project.models.services;

import project.models.dtos.InsuranceDTO;

import java.util.List;

public interface InsuranceService {

    void newInsurance (InsuranceDTO dto);

    List<InsuranceDTO> getAllInsurances();
}
