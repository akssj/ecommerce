package alledrogo.data.enums;

import org.springframework.security.core.GrantedAuthority;

public enum UserStatus implements GrantedAuthority {
    STATUS_ACTIVE,
    STATUS_DELETED,
    STATUS_BANNED;

    @Override
    public String getAuthority() {
        return name();
    }
}
