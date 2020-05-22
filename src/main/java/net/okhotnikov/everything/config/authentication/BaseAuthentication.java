package net.okhotnikov.everything.config.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Created by Sergey Okhotnikov.
 */
public class BaseAuthentication implements Authentication {

    protected Collection<? extends GrantedAuthority> authorities;

    protected boolean isAuthenticated;

    protected UserDetails principal;

    public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
        this.authorities = authorities;
    }

    public void setPrincipal(UserDetails principal) {
        this.principal = principal;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return principal ;
    }

    @Override
    public Object getDetails() {
        return principal;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean b) throws IllegalArgumentException {
        isAuthenticated=b;
    }

    @Override
    public String getName() {
        return principal.getUsername();
    }
}
