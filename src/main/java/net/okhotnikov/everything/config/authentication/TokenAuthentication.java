package net.okhotnikov.everything.config.authentication;

/**
 * Created by Sergey Okhotnikov.
 */
public class TokenAuthentication extends BaseAuthentication {

    protected String token;

    public TokenAuthentication(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
