package project.models.services;

import project.models.dtos.InsuranceDTO;

import java.util.List;
import java.util.Optional;

public interface InsuranceService {

    void newInsurance (InsuranceDTO dto);

    List<InsuranceDTO> getAllInsurances();

    Optional<InsuranceDTO> getInsuranceById (long id);

    List<InsuranceDTO> getInsurancesByClientId(long clientId);
}
