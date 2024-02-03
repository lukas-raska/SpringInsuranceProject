package project.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.data.entities.insurance.InsuranceEntity;

import java.util.List;
import java.util.Optional;

/**
 * Rozhraní pro práci s databázovou vrstvou pro {@InsuranceEntity}
 */
@Repository
public interface InsuranceRepository extends JpaRepository<InsuranceEntity, Long> {

    Optional<List<InsuranceEntity>> findByInsuredClientId(long clientId);
}
