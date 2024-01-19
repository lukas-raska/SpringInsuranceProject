package project.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import project.data.entities.ClientEntity;

import java.util.Optional;

public interface ClientRepository extends JpaRepository<ClientEntity, Long> {

    Optional<ClientEntity> findByEmail(String username);

    default boolean existsByEmail(String email) {
        return false;
    }
}
