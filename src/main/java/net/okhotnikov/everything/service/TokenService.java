package net.okhotnikov.everything.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import net.okhotnikov.everything.model.TokenType;
import net.okhotnikov.everything.util.StringUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sergey Okhotnikov.
 */
@Service
public class TokenService {
    @Value("${token.secret}")
    private String tokenSecret;

    @Value("${token.ttl}")
    private int tokenTtl;

    @Value("${token.refresh.secret}")
    private String refreshSecret;

    @Value("${token.refresh.ttl}")
    private int refreshTtl;

    @Value("${reader.token.length}")
    private int readerTokenLength;

    public  String getToken(String username, TokenType tokenType){
        if(tokenType == TokenType.ACCESS_CODE){
            return StringUtil.getName(readerTokenLength);
        }


        Map<String, Object> tokenData = new HashMap<>();

        tokenData.put("clientType", "user");
        tokenData.put("username", username);
        tokenData.put("token_create_date", new Date().getTime());
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, getTtl(tokenType));

        return  Jwts.builder()
                .setClaims(tokenData)
                .setExpiration(calendar.getTime())
                .setSubject("everything")
                .signWith(SignatureAlgorithm.HS512, getSecret(tokenType))
                .compact();
    }

    private String getSecret(TokenType tokenType) {
        switch (tokenType){
            case REFRESH:
                return refreshSecret;
            case BEARER:
            default:
                return tokenSecret;
        }
    }

    private int getTtl(TokenType tokenType) {
        switch (tokenType){
            case REFRESH:
                return refreshTtl;
            case BEARER:
            default:
                return tokenTtl;
        }
    }


    public String getTokenSecret() {
        return tokenSecret;
    }

    public int getTokenTtl() {
        return tokenTtl;
    }

    public String getRefreshSecret() {
        return refreshSecret;
    }

    public int getRefreshTtl() {
        return refreshTtl;
    }

    public int getReaderTokenLength() {
        return readerTokenLength;
    }
}
