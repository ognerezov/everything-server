package net.okhotnikov.everything.config.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.okhotnikov.everything.exceptions.UnauthorizedException;
import net.okhotnikov.everything.exceptions.rejection.RejectReason;
import net.okhotnikov.everything.exceptions.rejection.Rejection;
import net.okhotnikov.everything.model.User;
import net.okhotnikov.everything.service.RedisService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

/**
 * Created by Sergey Okhotnikov.
 */
@Service
public class TokenAuthenticationManager implements AuthenticationManager {

    private final RedisService redisService;
    private final ObjectMapper mapper;

    public TokenAuthenticationManager(RedisService redisService, ObjectMapper mapper) {
        this.redisService = redisService;
        this.mapper = mapper;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if(! (authentication instanceof TokenAuthentication))
            throw  UnauthorizedException.ofReason(
                    new Rejection(
                            RejectReason.WrongAuthenticationFormat,
                            authentication.getClass().getSimpleName())
                    ,mapper);
        TokenAuthentication tokenAuthentication = (TokenAuthentication) authentication;
        User user = redisService.auth(tokenAuthentication.token);
        if(user == null)
            throw UnauthorizedException.ofReason(
                    new Rejection(
                            RejectReason.UserNotFound,
                            tokenAuthentication.token)
                    ,mapper);
        return new TokenAuthentication(user,tokenAuthentication.token);
    }

}
