package net.okhotnikov.everything.api.out;

/**
 * Created by Sergey Okhotnikov.
 */
public class RegisterResponse extends TokenResponse{
    public String accessCode;

    public String getAccessCode() {
        return accessCode;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public RegisterResponse() {
    }

    public RegisterResponse(String token, String refreshToken, String accessCode, String username, String emailStatus) {
        super(token, refreshToken, username, emailStatus);
        this.accessCode = accessCode;
    }
}
