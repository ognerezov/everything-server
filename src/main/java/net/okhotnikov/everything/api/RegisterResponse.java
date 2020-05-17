package net.okhotnikov.everything.api;

/**
 * Created by Sergey Okhotnikov.
 */
public class RegisterResponse extends TokenResponse{
    public String readersToken;

    public String getReadersToken() {
        return readersToken;
    }

    public void setReadersToken(String readersToken) {
        this.readersToken = readersToken;
    }

    public RegisterResponse() {
    }

    public RegisterResponse(String token, String refreshToken, String readersToken) {
        super(token, refreshToken);
        this.readersToken = readersToken;
    }
}
