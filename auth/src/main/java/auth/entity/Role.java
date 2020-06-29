package auth.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_ADMIN, ROLE_MANAGER, ROLE_SELLER, ROLE_CUSTOMER;

    public String getAuthority() {
        return name();
    }
}
