package trade_point.entity;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    ROLE_ADMIN, ROLE_MANAGER, ROLE_SELLER;

    public String getAuthority() {
        return name();
    }

    public static Role getRole(String s){
        switch (s) {
            case "ROLE_ADMIN": return ROLE_ADMIN;
            case "ROLE_MANAGER": return ROLE_MANAGER;
            case "ROLE_SELLER": return ROLE_SELLER;
            default: return null;
        }
    }
}
