package project.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.data.entities.insurance.InsuranceEntity;

import java.util.List;
import java.util.Optional;

public interface InsuranceRepository extends JpaRepository<InsuranceEntity, Long> {

    Optional<List<InsuranceEntity>> findByInsuredClientId(long clientId);
}
