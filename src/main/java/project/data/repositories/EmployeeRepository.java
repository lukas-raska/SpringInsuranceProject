package project.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.data.entities.EmployeeEntity;

import java.util.Optional;

/**
 * Rozhraní pro práci s databázovou vrstvou pro {@EmployeeEntity}
 */
@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

    Optional<EmployeeEntity> findByEmail(String username);
}
