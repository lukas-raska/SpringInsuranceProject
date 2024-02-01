package project.models.services;

import project.models.dtos.InsuranceDTO;

import java.util.List;

/**
 * Rozhraní služby pro operace týkající se pojistek
 */
public interface InsuranceService {

    /**
     * Vytvoří nové pojištění
     * @param dto {@link InsuranceDTO}
     */
    void newInsurance (InsuranceDTO dto);

    /**
     * Slouží k získání všech záznamů pojistek z databáze
     * @return List {@link InsuranceDTO}
     */
    List<InsuranceDTO> getAllInsurances();

    /**
     * Získá záznam o pojistce dle zadaného identifikátoru
     * @param id - identifikátor pojistky
     * @return - nalezenou pojistku ve formátu {@link InsuranceDTO}
     */
   InsuranceDTO getInsuranceById (long id);

    /**
     * Získá seznam všech pojistek patřících konkrétnímu klientovi
     * @param clientId - identifikátor klienta
     * @return List {@link InsuranceDTO}
     */
    List<InsuranceDTO> getInsurancesByClientId(long clientId);

    /**
     * Upraví informace o uložené pojistce
     * @param dto {@link InsuranceDTO}
     */
    void editInsurance (InsuranceDTO dto);

    /**
     * Odstraní záznam o uložené pojistce
     * @param id {@link InsuranceDTO}
     */
    void deleteInsurance(Long id);
}
