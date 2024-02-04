package project.constant;

import lombok.Getter;

@Getter
public enum UserRole {

    ROLE_ADMIN ("admin"),
    ROLE_CLIENT ("klient"),
    ROLE_EMPLOYEE ("zaměstnanec"),
    UNKNOWN("unknown user")
    ;

    private final String name;

    UserRole(String name) {
        this.name = name;
    }


}
