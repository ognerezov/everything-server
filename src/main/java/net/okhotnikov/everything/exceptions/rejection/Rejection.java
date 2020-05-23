package net.okhotnikov.everything.exceptions.rejection;

/**
 * Created by Sergey Okhotnikov.
 */
public class Rejection {
    public RejectReason error;
    public String message;
    public final int status = 401;
    public final long timestamp = System.currentTimeMillis();

    public Rejection() {
    }

    public Rejection(RejectReason error) {
        this.error = error;
    }

    public Rejection(RejectReason error, String message) {
        this.error = error;
        this.message = message;
    }

    public RejectReason getError() {
        return error;
    }

    public void setError(RejectReason error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
