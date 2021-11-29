package net.okhotnikov.everything.api.in;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

/**
 * Created by Sergey Okhotnikov.
 */
public class RegisterRequest {
    @NotNull @Email
    public String username;
    @NotNull
    public String password;

    public String app;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }
}
