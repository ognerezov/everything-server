package net.okhotnikov.everything.config.authentication;

import net.okhotnikov.everything.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Created by Sergey Okhotnikov.
 */
public class BaseAuthentication implements Authentication {

    public BaseAuthentication() {
    }

    public BaseAuthentication(User user) {
        this.authorities = user.roles;
        this.isAuthenticated = user.enabled;
        this.principal = user;
    }

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
