package net.okhotnikov.everything.config.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Sergey Okhotnikov.
 */
@Configuration
public class TokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private final TokenAuthenticationProvider tokenAuthenticationProvider;

    protected TokenAuthenticationFilter(TokenAuthenticationProvider tokenAuthenticationProvider) {
        super("/**");

        setAuthenticationSuccessHandler((request, response, authentication) ->
        {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            request.getRequestDispatcher(request.getServletPath() ).forward(request, response);
        });
        setAuthenticationFailureHandler((request, response, authenticationException) -> {
            response.getOutputStream().print(authenticationException.getMessage());
        });
        this.tokenAuthenticationProvider = tokenAuthenticationProvider;
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null && authentication.isAuthenticated())
            chain.doFilter(req,res);
        super.doFilter(req, res,chain);
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        String token = request.getHeader("Authorization");
        return tokenAuthenticationProvider.authenticate(new TokenAuthentication(token));
    }

    @Autowired
    @Override
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }
}
