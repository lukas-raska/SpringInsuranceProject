package project.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.data.entities.insurance.InsuranceEntity;
import project.data.repositories.ClientRepository;
import project.data.repositories.InsuranceRepository;
import project.models.dtos.InsuranceDTO;
import project.models.dtos.mappers.InsuranceMapper;
import project.models.exceptions.InsuranceNotFoundException;
import project.models.exceptions.WrongInsuranceDatesException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class InsuranceServiceImpl implements InsuranceService {

    @Autowired
    private InsuranceMapper insuranceMapper;
    @Autowired
    private InsuranceRepository insuranceRepository;
    @Autowired
    private ClientRepository clientRepository;

    @Override
    public void newInsurance(InsuranceDTO dto) {
        if (dto.getInsuranceEnd().isBefore(dto.getInsuranceStart())) {
            throw new WrongInsuranceDatesException();
        }
        InsuranceEntity newInsurance = new InsuranceEntity();
        newInsurance = insuranceMapper.dtoToEntity(dto);
        newInsurance.setInsuredClient(clientRepository.getReferenceById(dto.getClientId()));
        newInsurance.setDateOfRegistry(LocalDate.now());

        insuranceRepository.save(newInsurance);

    }

    @Override
    public List<InsuranceDTO> getAllInsurances() {
        return insuranceRepository
                .findAll()
                .stream()
                .map(insuranceEntity -> insuranceMapper.entityToDTO(insuranceEntity))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<InsuranceDTO> getInsuranceById(long id) {
        InsuranceEntity fetchedEntity = insuranceRepository.findById(id).orElseThrow(InsuranceNotFoundException::new);
        return Optional.of(insuranceMapper.entityToDTO(fetchedEntity));
    }
}
