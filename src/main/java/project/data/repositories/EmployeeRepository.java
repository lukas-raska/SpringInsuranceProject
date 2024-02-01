package project.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.data.entities.EmployeeEntity;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

    Optional<EmployeeEntity> findByEmail(String username);
}
