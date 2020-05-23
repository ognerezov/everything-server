package net.okhotnikov.everything.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.okhotnikov.everything.exceptions.rejection.Rejection;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Sergey Okhotnikov.
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends AuthenticationException {
   public UnauthorizedException(String msg) {
        super(msg);
    }

    public static UnauthorizedException ofReason(Rejection rejection, ObjectMapper mapper){
       String msg = "";
        try {
            msg = mapper.writeValueAsString(rejection);
        } catch (JsonProcessingException e) {

        }
        return new UnauthorizedException(msg);
    }

    public static String getMessage(Rejection rejection, ObjectMapper mapper, String def){
        try {
            return mapper.writeValueAsString(rejection);
        } catch (JsonProcessingException e) {
            return def;
        }
    }
}
