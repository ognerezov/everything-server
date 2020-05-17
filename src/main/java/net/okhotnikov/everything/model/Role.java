package net.okhotnikov.everything.model;

import org.springframework.security.core.GrantedAuthority;

/**
 * Created by Sergey Okhotnikov.
 */
public enum Role implements GrantedAuthority {
    ROLE_USER,
    ROLE_MEMBER,
    ROLE_READER,
    ROLE_ADMIN,
    ROLE_ROOT;

    @Override
    public String getAuthority() {
        return name();
    }
}