package net.okhotnikov.everything.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static net.okhotnikov.everything.util.Literals.DATE_FORMAT;

/**
 * Created by Sergey Okhotnikov.
 */
public class User extends UserRecord implements UserDetails {
    public String password;
    public String refreshToken;
    public LocalDate registered = LocalDate.now();

    public User() {
    }

    public User(String username, String password, Set<Role> roles, boolean enabled) {
        this.username = username;
        this.password = password;
        this.roles = roles;
        this.enabled = enabled;
    }

    public User(User user) {
        this(user.username,user.password, new HashSet<>(user.roles),user.enabled);
        this.token = user.token;
        this.refreshToken = user.token;
        this.registered = user.registered;
    }

    public User withEncodedPassword(PasswordEncoder encoder){
        User res = new User(this);
        res.password = encoder.encode(password);
        return res;
    }

    public UserRecord toRecord(){
        return new UserRecord(username,roles, enabled);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DATE_FORMAT)
    public LocalDate getRegistered() {
        return registered;
    }

    public void setRegistered(LocalDate registered) {
        this.registered = registered;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return enabled;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return enabled;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return enabled;
    }

    @Override
    public String toString() {
        return "User{" +
                "password='" + password + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", registered=" + registered +
                ", username='" + username + '\'' +
                ", roles=" + roles +
                ", token='" + token + '\'' +
                ", enabled=" + enabled +
                "} " + super.toString();
    }
}
