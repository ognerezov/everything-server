package net.okhotnikov.everything.config.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.okhotnikov.everything.exceptions.UnauthorizedException;
import net.okhotnikov.everything.exceptions.rejection.RejectReason;
import net.okhotnikov.everything.exceptions.rejection.Rejection;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Sergey Okhotnikov.
 */
@Configuration
public class TokenAuthenticationFilter implements Filter {

    public static final String AUTHORIZATION = "Authorization";
    private final TokenAuthenticationProvider tokenAuthenticationProvider;
    private final ObjectMapper mapper;

    protected TokenAuthenticationFilter(TokenAuthenticationProvider tokenAuthenticationProvider, ObjectMapper mapper) {
        this.mapper = mapper;
        this.tokenAuthenticationProvider = tokenAuthenticationProvider;
    }
    private void returnUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON.toString());
        response.getOutputStream().print(message);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response = (HttpServletResponse)res;

        String path = request.getServletPath();
        if(!path.startsWith("/book") && !path.startsWith("/admin")){
            chain.doFilter(req,res);
            return;
        }

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
    }

    private String getToken(HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION);
    }
}
