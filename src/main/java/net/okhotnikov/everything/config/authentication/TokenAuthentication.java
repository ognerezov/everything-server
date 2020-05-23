package net.okhotnikov.everything.config.authentication;

import net.okhotnikov.everything.model.User;

/**
 * Created by Sergey Okhotnikov.
 */
public class TokenAuthentication extends BaseAuthentication {

    protected String token;

    public TokenAuthentication(String token) {
        this.token = token;
    }

    public TokenAuthentication(User user, String token) {
        super(user);
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
