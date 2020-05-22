package net.okhotnikov.everything.config.authentication;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

/**
 * Created by Sergey Okhotnikov.
 */
@Service
public class TokenAuthenticationManager implements AuthenticationManager {

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        System.out.println(authentication);
        authentication.setAuthenticated(true);
        return authentication;
    }

}
