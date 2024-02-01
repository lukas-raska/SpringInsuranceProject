package project.data.entities;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

/**
 * Entitní třída reprezentující zaměstnance
 */
@Entity(name = "employee")
@Getter
@Setter
public class EmployeeEntity extends BaseUserEntity {

    private boolean admin;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ " + (this.isAdmin() ? "ADMIN" : "EMPLOYEE"));
        return List.of(authority);
    }
}
