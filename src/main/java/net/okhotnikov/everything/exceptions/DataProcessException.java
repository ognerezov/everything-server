package net.okhotnikov.everything.exceptions;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.NOT_ACCEPTABLE;

/**
 * Created by Sergey Okhotnikov.
 */
@ResponseStatus(NOT_ACCEPTABLE)
public class DataProcessException extends RuntimeException {
    public DataProcessException(String message) {
        super(message);
    }
}
