package net.okhotnikov.everything.api.out;

/**
 * Created by Sergey Okhotnikov.
 */
public class TokenResponse {
    public String token;
    public String refreshToken;
    public String username;
    public String emailStatus;

    public TokenResponse() {
    }

    public TokenResponse(String token, String refreshToken, String username, String emailStatus) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.username = username;
        this.emailStatus = emailStatus;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmailStatus() {
        return emailStatus;
    }

    public void setEmailStatus(String emailStatus) {
        this.emailStatus = emailStatus;
    }
}
