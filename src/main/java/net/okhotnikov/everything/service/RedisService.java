package net.okhotnikov.everything.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.okhotnikov.everything.api.out.TokenResponse;
import net.okhotnikov.everything.dao.RedisDao;
import net.okhotnikov.everything.exceptions.DataProcessException;
import net.okhotnikov.everything.exceptions.UnauthorizedException;
import net.okhotnikov.everything.exceptions.service.NotVerifiedEmailException;
import net.okhotnikov.everything.model.TokenType;
import net.okhotnikov.everything.model.User;
import net.okhotnikov.everything.model.UserRecord;
import org.springframework.stereotype.Service;

import static net.okhotnikov.everything.util.Literals.EMAIL_NOT_SENT_STATUS;
import static net.okhotnikov.everything.util.Literals.EMAIL_SENT_STATUS;

/**
 * Created by Sergey Okhotnikov.
 */
@Service
public class RedisService {

    private final RedisDao dao;
    private final ObjectMapper mapper;
    private final TokenService tokenService;

    public RedisService(RedisDao dao, ObjectMapper mapper, TokenService tokenService) {
        this.dao = dao;
        this.mapper = mapper;
        this.tokenService = tokenService;
    }

    public TokenResponse login(User user){
        return login(user,TokenType.BEARER);
    }

    public TokenResponse login(User user, TokenType tokenType){
        String token = tokenService.getToken(user.username, tokenType);
        String refreshToken = tokenService.getToken(user.username,TokenType.REFRESH);

        try {
            UserRecord userRecord = user.toRecord();
            userRecord.token = token;
            String json = mapper.writeValueAsString(userRecord);
            dao.putString(
                    token,
                    json,
                    tokenService.getTokenTtl()*60
            );

            /*
             * that record should not be used for direct authentication
             */
            userRecord.enabled = false;
            json = mapper.writeValueAsString(userRecord);
            dao.putString(
                    refreshToken,
                    json,
                    tokenService.getRefreshTtl() * 60);

            return new TokenResponse(token,refreshToken, user.username, user.emailStatus);
        } catch (JsonProcessingException e) {
           throw new  DataProcessException(e.getClass().getSimpleName());
        }
    }

    public User auth(String token){
        User user = null;
        try {
            String stored =dao.getString(token);
            if(stored == null)
                return null;
            user = mapper.readValue(stored, User.class);
        } catch (JsonProcessingException e) {
            return null;
        }
        if(user == null)
            return null;

        user.enabled = true;

        return user;
    }

    public TokenResponse refresh(String refreshToken) throws NotVerifiedEmailException {
        return refresh(refreshToken,TokenType.BEARER);
    }

    public TokenResponse refresh(String refreshToken, TokenType tokenType) throws NotVerifiedEmailException {
        User user = auth(refreshToken);
        if(user == null)
            throw new UnauthorizedException(refreshToken);
        if(user.emailStatus == null || user.emailStatus.equals(EMAIL_SENT_STATUS) || user.emailStatus.equals(EMAIL_NOT_SENT_STATUS))
            throw new NotVerifiedEmailException();

        dao.delKey(refreshToken);
        dao.delKey(user.token);

        return login(user, tokenType);
    }

    public void revokeTokens(User user) {
        dao.delKey(user.token);
        dao.delKey(user.refreshToken);
    }
}
