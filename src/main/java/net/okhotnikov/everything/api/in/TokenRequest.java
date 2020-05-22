package net.okhotnikov.everything.api.in;

import javax.validation.constraints.NotNull;

/**
 * Created by Sergey Okhotnikov.
 */
public class TokenRequest {
    @NotNull
    public String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
