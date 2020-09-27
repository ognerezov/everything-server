package net.okhotnikov.everything.api.in;

import java.util.Map;

public class AppleVerificationResponse extends StatusResponse {
    public Map<String,Object> receipt;
    public String environment;

    public Map<String, Object> getReceipt() {
        return receipt;
    }

    public void setReceipt(Map<String, Object> receipt) {
        this.receipt = receipt;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }
}
