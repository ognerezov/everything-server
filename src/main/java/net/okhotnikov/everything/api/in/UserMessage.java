package net.okhotnikov.everything.api.in;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class UserMessage {
    @NotEmpty
    @Email
    public String email;
    @NotEmpty
    public String message;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
