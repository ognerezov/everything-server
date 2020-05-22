package net.okhotnikov.everything.config.authentication;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

/**
 * Created by Sergey Okhotnikov.
 */
@Service
public class TokenAuthenticationProvider implements AuthenticationProvider {

    private final TokenAuthenticationManager tokenAuthenticationManager;

    public TokenAuthenticationProvider(TokenAuthenticationManager tokenAuthenticationManager) {
        this.tokenAuthenticationManager = tokenAuthenticationManager;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return tokenAuthenticationManager.authenticate(authentication);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
