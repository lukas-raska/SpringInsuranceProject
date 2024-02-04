package project.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.data.entities.InsuranceEntity;
import project.data.repositories.ClientRepository;
import project.data.repositories.InsuranceRepository;
import project.models.dtos.InsuranceDTO;
import project.models.dtos.mappers.InsuranceMapper;
import project.models.exceptions.ClientNotFoundException;
import project.models.exceptions.InsuranceNotFoundException;
import project.models.exceptions.WrongInsuranceDatesException;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Implementace rozhraní {@link InsuranceService} sloužící pro manipulaci se záznamy o pojistkách
 */
@Service
public class InsuranceServiceImpl implements InsuranceService {

    @Autowired
    private InsuranceMapper insuranceMapper;
    @Autowired
    private InsuranceRepository insuranceRepository;
    @Autowired
    private ClientRepository clientRepository;

    /**
     * Vytvoří nový záznam - pojištění
     *
     * @param dto {@link InsuranceDTO} - data z formuláře
     */
    @Override
    public void newInsurance(InsuranceDTO dto) {
        if (dto.getInsuranceEnd().isBefore(dto.getInsuranceStart())) {
            throw new WrongInsuranceDatesException();
        }
        InsuranceEntity newInsurance = insuranceMapper.dtoToEntity(dto);
        newInsurance.setInsuredClient(clientRepository.getReferenceById(dto.getClientId()));
        newInsurance.setDateOfRegistry(LocalDate.now());

        insuranceRepository.save(newInsurance);

    }

    /**
     * Získá všechny záznamy - pojištění z databáze
     *
     * @return - seznam {@link InsuranceDTO}
     */
    @Override
    public List<InsuranceDTO> getAllInsurances() {
        return insuranceRepository
                .findAll()
                .stream()
                .map(insuranceEntity -> insuranceMapper.entityToDTO(insuranceEntity))
                .collect(Collectors.toList());
    }

    /**
     * Získá záznam o pojistce dle unikátního identifikátoru
     *
     * @param id - identifikátor pojistky
     * @return {@link}
     */
    @Override
    public InsuranceDTO getInsuranceById(long id) {
        InsuranceEntity fetchedEntity = insuranceRepository.findById(id).orElseThrow(InsuranceNotFoundException::new);
        return insuranceMapper.entityToDTO(fetchedEntity);
    }

    /**
     * Získá všechny záznamy - pojistky dle konkrétního klienta
     *
     * @param clientId - identifikátor klienta
     * @return Seznam pojistek ve formátu {@link InsuranceDTO}
     */
    @Override
    public List<InsuranceDTO> getInsurancesByClientId(long clientId) {
        return insuranceRepository
                .findByInsuredClientId(clientId)
                .orElseThrow(ClientNotFoundException::new)
                .stream()
                .map(entity -> insuranceMapper.entityToDTO(entity))
                .collect(Collectors.toList());
    }

    /**
     * Edituje existující záznam
     *
     * @param dto {@link InsuranceDTO}
     */
    @Override
    public void editInsurance(InsuranceDTO dto) {

        InsuranceEntity fetchedEntity = insuranceRepository
                .findById(dto.getId())
                .orElseThrow(InsuranceNotFoundException::new);

        insuranceMapper.updateInsuranceEntity(dto, fetchedEntity);
        insuranceRepository.save(fetchedEntity);

    }

    /**
     * Odstraní záznam - pojistku se zadaným identifikátorem
     *
     * @param id {@link InsuranceDTO}
     */
    @Override
    public void deleteInsurance(Long id) {
        if (id == null) {
            throw new InsuranceNotFoundException();
        }
        insuranceRepository.deleteById(id);
    }
}
