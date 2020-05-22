package net.okhotnikov.everything.api.out;

/**
 * Created by Sergey Okhotnikov.
 */
public class TokenResponse {
    public String token;
    public String refreshToken;

    public TokenResponse() {
    }

    public TokenResponse(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
