package net.okhotnikov.everything.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.PAYMENT_REQUIRED)
public class UnverifiedPurchaseException extends RuntimeException {
    public UnverifiedPurchaseException(String message) {
        super(message);
    }
}
