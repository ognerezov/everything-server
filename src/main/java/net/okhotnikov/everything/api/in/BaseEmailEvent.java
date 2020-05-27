package net.okhotnikov.everything.api.in;

/**
 * Created by Sergey Okhotnikov.
 */
public class BaseEmailEvent {
    public String email;
    public String event;
    public String reason;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
