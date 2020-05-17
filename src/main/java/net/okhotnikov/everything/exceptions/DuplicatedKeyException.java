package net.okhotnikov.everything.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Sergey Okhotnikov.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class DuplicatedKeyException extends  RuntimeException{
    public DuplicatedKeyException(String message) {
        super(message);
    }
}
