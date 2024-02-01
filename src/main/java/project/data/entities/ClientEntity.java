package project.data.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import project.data.entities.insurance.InsuranceEntity;

import java.util.Collection;
import java.util.List;

/**
 * Entitní třída reprezentující klienta
 */
@Entity(name = "client")
@Getter
@Setter
public class ClientEntity extends BaseUserEntity {

    @OneToMany(mappedBy = "insuredClient", cascade = CascadeType.ALL)
    private List<InsuranceEntity> contractedInsurances;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_CLIENT");
        return List.of(authority);
    }
}


