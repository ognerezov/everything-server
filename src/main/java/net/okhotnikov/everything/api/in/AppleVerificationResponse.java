package net.okhotnikov.everything.api.in;

import java.util.List;
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

    public String transactionId(){
        String res = null;

        List<Map<String, Object>> inApp = (List<Map<String,Object>>)receipt.get("in_app");
        res = (String) inApp.get(0).get("original_transaction_id");
        return res;
    }

    @Override
    public String toString() {
        return "AppleVerificationResponse{" +
                "receipt=" + receipt +
                ", environment='" + environment + '\'' +
                ", status=" + status +
                '}';
    }
}
