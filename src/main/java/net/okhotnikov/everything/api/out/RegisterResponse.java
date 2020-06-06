package net.okhotnikov.everything.api.out;

import net.okhotnikov.everything.model.Role;

import java.util.Set;

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

    public RegisterResponse(String token, String refreshToken, String accessCode, String username, String emailStatus, Set<Role> roles) {
        super(token, refreshToken, username, emailStatus,roles);
        this.accessCode = accessCode;
    }
}
