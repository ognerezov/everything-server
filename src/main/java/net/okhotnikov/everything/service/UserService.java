package net.okhotnikov.everything.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.okhotnikov.everything.api.out.RegisterResponse;
import net.okhotnikov.everything.api.out.TokenResponse;
import net.okhotnikov.everything.dao.ElasticDao;
import net.okhotnikov.everything.dao.RedisDao;
import net.okhotnikov.everything.exceptions.DuplicatedKeyException;
import net.okhotnikov.everything.exceptions.NotFoundException;
import net.okhotnikov.everything.exceptions.UnauthorizedException;
import net.okhotnikov.everything.exceptions.service.NotVerifiedEmailException;
import net.okhotnikov.everything.model.Role;
import net.okhotnikov.everything.model.TokenType;
import net.okhotnikov.everything.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Email;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static net.okhotnikov.everything.util.Literals.*;
import static net.okhotnikov.everything.service.ElasticService.*;

/**
 * Created by Sergey Okhotnikov.
 */
@Service
public class UserService {

    public static final String EMAIL_STATUS = "emailStatus";
    public static final String REASON = "reason";
    private final ElasticService elasticService;
    private final ElasticDao dao;
    private final ObjectMapper mapper;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;
    private final EmailService emailService;
    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
    private final TokenService tokenService;

    @Value("${reader.password}")
    private String readerPassword;

    @Value("${reader.username}")
    private String readerUsername;

    @Value("${reader.trial}")
    private int trialPeriod;


    public UserService(ElasticService elasticService, ElasticDao dao, ObjectMapper mapper, PasswordEncoder passwordEncoder, RedisService redisService, EmailService emailService, TokenService tokenService) {
        this.elasticService = elasticService;
        this.dao = dao;
        this.mapper = mapper;
        this.passwordEncoder = passwordEncoder;
        this.redisService = redisService;
        this.emailService = emailService;
        this.tokenService = tokenService;
    }

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
                true,
                EMAIL_SENT_STATUS
        );
        TokenResponse response = redisService.login(user, TokenType.BEARER);
        String readersToken = getReadersToken();

        user.token = response.token;
        user.refreshToken = response.refreshToken;

        try {
            create(user);
            emailService.send(username,readersToken);

        } catch (Exception e){
            deletePreviousTokens(user);

            throw e;
        }

        return new RegisterResponse(response.token, response.refreshToken,null,username, EMAIL_SENT_STATUS,user.roles);
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
            throw new UnauthorizedException(username);

        deletePreviousTokens(user);
        TokenResponse response = redisService.login(user, tokenType);
        setTokens(username, response.token, response.refreshToken);

        return response;
    }

    private void deletePreviousTokens(User user) {
        redisService.revokeTokens(user);
    }

    private void setTokens(String username, String token, String refreshToken) throws IOException {
        Map<String, Object> data = new HashMap<>();

        data.put(TOKEN,token);
        data.put(REFRESH_TOKEN,refreshToken);

        dao.update(USERS,username,data);
    }

    public void setEmailStatus(String username, String status, String reason) throws IOException {
        Map<String, Object> data = new HashMap<>();

        data.put(EMAIL_STATUS,status);
        data.put(REASON,reason);

        dao.update(USERS,username,data);
    }

    public User auth(String token){
        return redisService.auth(token);
    }

    public TokenResponse refresh(String token) throws IOException {
        try {
            return redisService.refresh(token);
        }catch (NotVerifiedEmailException e){
            String username = auth(token).username;
            User user = get(username);
            if (user == null)
                throw new UnauthorizedException(token);
            redisService.revokeTokens(user);
            return redisService.login(user);
        }catch (UnauthorizedException exception){
            try {
                User user = elasticService.getByUniqueField(USERS, REFRESH_TOKEN, token, new TypeReference<User>() {});
                if (user != null){
                    redisService.revokeTokens(user);
                    System.out.println("Erase tokens for user: "+ user.username);
                    setTokens(user.username,null,null);
                }
            }catch (IndexOutOfBoundsException ignored){

            }
            throw exception;
        }
    }

    public TokenResponse restore(String token) throws IOException {
        User user = auth(token);
        deletePreviousTokens(user);
        user.token = tokenService.getToken(user.username, TokenType.BEARER);
        user.refreshToken = tokenService.getToken(user.username,TokenType.REFRESH);
        setTokens(user.username,user.token,user.refreshToken);
        redisService.putUser(user);
        redisService.delete(token);
        return new TokenResponse(user.token,user.refreshToken, user.username, user.emailStatus,user.roles);
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

    public String updateReader() throws IOException{
        TokenResponse tokenResponse = loginReader();
        LocalDate date = LocalDate.now();

        List<User> res = getAfter(date.minus(trialPeriod, ChronoUnit.DAYS));
        for(User user: res){
            try {

                emailService.sendRenew(user.username,tokenResponse.token);

            }catch (Exception e){
                LOG.error((user == null? "null" : user.username) +" -> "+e.getClass().getSimpleName());
            }
        }
        return tokenResponse.token;
    }

    public User addRole(String username, Role role) throws IOException {
        User user = get(username);
        if(user == null)
            throw new NotFoundException();
        user.roles.add(role);
        storeUserRoles(username, user);

        return user;
    }

    public User removeRole(String username, Role role) throws  IOException{
        User user = get(username);
        if(user == null)
            throw new NotFoundException();
        user.roles.remove(role);
        storeUserRoles(username, user);
        return user;
    }

    private void storeUserRoles(String username, User user) throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put(ROLES, user.roles);
        dao.update(USERS, username, data);
        redisService.update(user);
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

    public void restoreReadersToken() throws IOException {
        User user = get(readerUsername);
        redisService.update(user,TokenType.ACCESS_CODE);
    }

    public String setTemporalCode(User user) throws IOException {
        user.token = tokenService.getToken(user.username,TokenType.TEMP_CODE);
        redisService.temp(user);
        emailService.sendTempCode(user.username, user.token);
        return user.token;
    }

    public void setPassword(String username, String password) throws IOException {
        Map<String, Object> data = new HashMap<>();

        data.put("password",passwordEncoder.encode(password));

        dao.update(USERS,username,data);
    }

}
