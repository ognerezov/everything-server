package net.okhotnikov.everything.api.in;

public class StatusResponse {
    public int status;

    public StatusResponse() {
    }

    public StatusResponse(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "StatusResponse{" +
                "status=" + status +
                '}';
    }
}
