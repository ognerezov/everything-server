package net.okhotnikov.everything.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Sergey Okhotnikov.
 */
public class UserRecord {
    public String username;
    public Set<Role> roles;
    public String token;
    public boolean enabled;
    public String emailStatus;
    public String app;

    public UserRecord() {
    }

    public UserRecord(String username, Set<Role> roles, boolean enabled, String emailStatus, String app) {
        this.username = username;
        this.roles = roles;
        this.enabled = enabled;
        this.emailStatus = emailStatus;
        this.app = app;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public static Set<Role> getUserRoles(){
        Set<Role> res = new HashSet<>();
        res.add(Role.ROLE_USER);
        return res;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public String getUsername() {
        return username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getEmailStatus() {
        return emailStatus;
    }

    public void setEmailStatus(String emailStatus) {
        this.emailStatus = emailStatus;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    @Override
    public String toString() {
        return "UserRecord{" +
                "username='" + username + '\'' +
                ", roles=" + roles +
                ", token='" + token + '\'' +
                ", enabled=" + enabled +
                ", bounced=" + emailStatus +
                '}';
    }
}
