package net.okhotnikov.everything.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.okhotnikov.everything.api.RegisterResponse;
import net.okhotnikov.everything.api.TokenResponse;
import net.okhotnikov.everything.dao.ElasticDao;
import net.okhotnikov.everything.dao.RedisDao;
import net.okhotnikov.everything.exceptions.DuplicatedKeyException;
import net.okhotnikov.everything.exceptions.UnauthorizedException;
import net.okhotnikov.everything.model.TokenType;
import net.okhotnikov.everything.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Email;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static net.okhotnikov.everything.util.Literals.*;
import static net.okhotnikov.everything.service.ElasticService.*;

/**
 * Created by Sergey Okhotnikov.
 */
@Service
public class UserService implements UserDetailsService {

    private final ElasticService elasticService;
    private final ElasticDao dao;
    private final ObjectMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;
    private final RedisDao redisDao;
    private final EmailService emailService;

    @Value("${reader.password}")
    private String readerPassword;

    @Value("${reader.username}")
    private String readerUsername;


    public UserService(ElasticService elasticService, ElasticDao dao, ObjectMapper mapper, PasswordEncoder passwordEncoder, RedisService redisService, RedisDao redisDao, EmailService emailService) {
        this.elasticService = elasticService;
        this.dao = dao;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
        this.redisService = redisService;
        this.redisDao = redisDao;
        this.emailService = emailService;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserDetails res;
        try {
            res = get(s);
            if (res == null)
                throw new UsernameNotFoundException(s);
            return res;
        } catch (IOException e) {
            throw new UsernameNotFoundException(s);
        }
    }


    public RegisterResponse register(@Email String username, String password) throws IOException {
        User user = new User(
                username,
                password,
                User.getUserRoles(),
                true
        );
        TokenResponse response = redisService.login(user, TokenType.BEARER);
        String readersToken = getReadersToken();

        user.token = response.token;
        user.refreshToken = response.refreshToken;

        try {
            create(user);
            emailService.send(username,readersToken);

        } catch (Exception e){
            redisDao.delKey(user.token);
            redisDao.delKey(user.refreshToken);

            throw e;
        }

        return new RegisterResponse(response.token, response.refreshToken,readersToken);
    }

    public User get(String username) throws IOException {
        return elasticService.get(username, USERS, new TypeReference<User>() {});
    }

    public void create(User user) throws IOException {
        User existing = get(user.username);

        if(existing != null)
            throw new DuplicatedKeyException(user.username);

        dao.put(
                ElasticService.USERS,
                user.username,
                mapper.writeValueAsString(user.withEncodedPassword(passwordEncoder))
        );
    }

    public void delete(String username) throws IOException {
        dao.delete(username, USERS);
    }

    public TokenResponse login(String username, String password) throws IOException {
        return login(username,password, TokenType.BEARER);
    }

    public TokenResponse login(String username, String password, TokenType tokenType) throws IOException {
        User user = get(username);

        if(user == null || ! passwordEncoder.matches(password,user.password))
            throw new UnauthorizedException();

        TokenResponse response = redisService.login(user, tokenType);

        setTokens(username, response.token, response.refreshToken);

        return response;
    }

    private void setTokens(String username, String token, String refreshToken) throws IOException {
        Map<String, Object> data = new HashMap<>();

        data.put(TOKEN,token);
        data.put(REFRESH_TOKEN,refreshToken);

        dao.update(USERS,username,data);
    }

    public User auth(String token){
        return redisService.auth(token);
    }

    public TokenResponse refresh(String token) throws IOException {
        try{
            return redisService.refresh(token);
        }catch (UnauthorizedException exception){
            User user = elasticService.getByUniqueField(USERS,REFRESH_TOKEN, token, new TypeReference<User>(){});
            if (user != null){
                redisService.revokeTokens(user);
                setTokens(user.username,null,null);
            }

            throw exception;
        }
    }

    public List<User> getAfter(LocalDate date) throws IOException {
        return elasticService.getAfter(
                USERS,
                REGISTERED,
                date,
                new TypeReference<User>() {});
    }

    public TokenResponse loginReader() throws IOException {
        return login(readerUsername, readerPassword, TokenType.ACCESS_CODE);
    }

    public String getReadersToken() throws IOException {
        User user = get(readerUsername);

        return user.token;
    }

    public String getReaderPassword() {
        return readerPassword;
    }

    public String getReaderUsername() {
        return readerUsername;
    }

}
