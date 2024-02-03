package project.data.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.data.entities.ClientEntity;

import java.util.Optional;

/**
 * Rozhraní pro práci s databázovou vrstvou pro {@ClientEntity}
 */
@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {

    /**
     * Vyhledá klienta podle emailové adresy
     * @param username email klienta
     * @return v případě nalezení klienta vrací jeho údaje
     */
    Optional<ClientEntity> findByEmail(String username);


}
