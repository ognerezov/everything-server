package net.okhotnikov.everything.config.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.okhotnikov.everything.exceptions.UnauthorizedException;
import net.okhotnikov.everything.exceptions.rejection.RejectReason;
import net.okhotnikov.everything.exceptions.rejection.Rejection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
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

    public static final String AUTHORIZATION = "Authorization";
    private final TokenAuthenticationProvider tokenAuthenticationProvider;
    private final ObjectMapper mapper;

    protected TokenAuthenticationFilter(TokenAuthenticationProvider tokenAuthenticationProvider, ObjectMapper mapper) {
        super("/**");
        this.mapper = mapper;

        setAuthenticationSuccessHandler((request, response, authentication) ->
        {
            SecurityContextHolder.getContext().setAuthentication(authentication);
 //           request.getRequestDispatcher(request.getServletPath() ).forward(request, response);
        });
        setAuthenticationFailureHandler((request, response, authenticationException) -> {
            returnUnauthorizedResponse(response, authenticationException.getMessage());
        });
        this.tokenAuthenticationProvider = tokenAuthenticationProvider;
    }

    private void returnUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getOutputStream().print(message);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)res;


        Authentication previousAuthentication = SecurityContextHolder.getContext().getAuthentication();

        String previousToken = null;

        if(previousAuthentication instanceof TokenAuthentication){
            previousToken = ((TokenAuthentication)previousAuthentication).token;
        }

        String token = getToken(request);
        if(token == null){
            returnUnauthorizedResponse(response,UnauthorizedException
                    .getMessage(
                            new Rejection(RejectReason.TokenIsNull)
                            ,mapper,""
                    ));
            return;
        }

        if(previousAuthentication != null && previousAuthentication.isAuthenticated() && token.equals(previousToken))
            chain.doFilter(req,res);
        else{
            try {
                Authentication authentication = tokenAuthenticationProvider.authenticate(new TokenAuthentication(token));
                SecurityContextHolder.getContext().setAuthentication(authentication);
                chain.doFilter(req,res);
            } catch (UnauthorizedException e){
                returnUnauthorizedResponse(response,e.getMessage());
            } catch (Exception exception){
                returnUnauthorizedResponse(response,UnauthorizedException
                        .getMessage(
                                new Rejection(RejectReason.ExceptionInProcess, exception.getClass().getSimpleName())
                                ,mapper,""
                        ));
            }

        }
           // super.doFilter(req, res,chain);
    }


    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse httpServletResponse) throws AuthenticationException, IOException, ServletException {
        String token = getToken(request);
        return tokenAuthenticationProvider.authenticate(new TokenAuthentication(token));
    }

    private String getToken(HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION);
    }

    @Autowired
    @Override
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        super.setAuthenticationManager(authenticationManager);
    }
}
