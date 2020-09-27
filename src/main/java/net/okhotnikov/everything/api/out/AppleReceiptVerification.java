package net.okhotnikov.everything.api.out;

import com.fasterxml.jackson.annotation.JsonProperty;

public class AppleReceiptVerification {
    private String receipt;

    public AppleReceiptVerification() {
    }

    public AppleReceiptVerification(String receipt) {
        this.receipt = receipt;
    }

    @JsonProperty("receipt-data")
    public String getReceipt() {
        return receipt;
    }

    @JsonProperty("receipt-data")
    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }
}
