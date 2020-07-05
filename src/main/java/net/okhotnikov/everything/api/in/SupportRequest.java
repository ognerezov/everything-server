package net.okhotnikov.everything.api.in;

import javax.validation.constraints.NotEmpty;

/**
 * Created by Sergey Okhotnikov.
 */
public class SupportRequest {
    public String theme;
    @NotEmpty
    public String message;

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
