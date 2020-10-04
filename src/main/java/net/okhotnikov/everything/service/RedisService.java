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
        user.token = tokenService.getToken(user.username, tokenType);
        user.refreshToken = tokenService.getToken(user.username,TokenType.REFRESH);
        try {
            update(user, tokenType);
            return new TokenResponse(user.token,user.refreshToken, user.username, user.emailStatus,user.roles);
        } catch (JsonProcessingException e) {
           throw new  DataProcessException(e.getClass().getSimpleName());
        }
    }

    public void update(User user) throws JsonProcessingException {
        update(user,TokenType.BEARER);
    }

    public void temp(User user){
        UserRecord userRecord = user.toRecord();
        try {
            String json = mapper.writeValueAsString(userRecord);
            dao.putString(
                    user.token,
                    json,
                    tokenService.getTempTtl()*60
            );
        } catch (JsonProcessingException e) {
            throw new RuntimeException();
        }

    }

    public void putUser(User user) throws JsonProcessingException {
        UserRecord userRecord = user.toRecord();
        String json = mapper.writeValueAsString(userRecord);
        dao.putString(
                user.token,
                json, tokenService.getTokenTtl()*60
        );
    }

    public void update(User user, TokenType tokenType) throws JsonProcessingException {
        UserRecord userRecord = user.toRecord();
        String json = mapper.writeValueAsString(userRecord);
        dao.putString(
                user.token,
                json,
                tokenType == TokenType.ACCESS_CODE ?
                        tokenService.getRefreshTtl() * 60 :
                        tokenService.getTokenTtl()*60
        );

        /*
         * that record should not be used for direct authentication
         */
        userRecord.enabled = false;
        json = mapper.writeValueAsString(userRecord);
        dao.putString(
                user.refreshToken,
                json,
                tokenService.getRefreshTtl() * 60);
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

        user.refreshToken =refreshToken;
        revokeTokens(user);

        return login(user, tokenType);
    }

    public void revokeTokens(User user) {
        if(user.token != null)
            dao.delKey(user.token);
        if(user.refreshToken != null)
            dao.delKey(user.refreshToken);
    }

    public void delete(String key){
        dao.delKey(key);
    }

    public String get(String key) {
        return dao.getString(key);
    }

    public void put(String key, String value) {
        dao.putString(key,value);
    }
}
