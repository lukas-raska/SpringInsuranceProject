package project.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.data.entities.insurance.InsuranceEntity;

public interface InsuranceRepository extends JpaRepository<InsuranceEntity, Long> {
}
