package net.okhotnikov.everything.exceptions.rejection;

/**
 * Created by Sergey Okhotnikov.
 */
public class Rejection {
    public RejectReason reason;
    public String details;

    public Rejection() {
    }

    public Rejection(RejectReason reason) {
        this.reason = reason;
    }

    public Rejection(RejectReason reason, String details) {
        this.reason = reason;
        this.details = details;
    }

    public RejectReason getReason() {
        return reason;
    }

    public void setReason(RejectReason reason) {
        this.reason = reason;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
