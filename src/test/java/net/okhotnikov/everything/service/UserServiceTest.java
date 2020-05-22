package net.okhotnikov.everything.service;

import static junit.framework.Assert.*;
import static org.junit.Assert.assertNotEquals;

import net.okhotnikov.everything.api.out.TokenResponse;
import net.okhotnikov.everything.dao.RedisDao;
import net.okhotnikov.everything.model.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.List;

/**
 * Created by Sergey Okhotnikov.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTest {

    public static final String TEST_USER_NAME = "ognerezov@yandex.ru";
    public static final String TEST_PASSWORD = "test";
    @Autowired
    private UserService userService;

    @Autowired
    private RedisDao redisDao;

    @Autowired
    private TokenService tokenService;

    @Test
    public void testCreateUser() throws IOException {

        userService.register(TEST_USER_NAME,TEST_PASSWORD);

        UserDetails stored = userService.loadUserByUsername(TEST_USER_NAME);

        System.out.println(stored);

        assertNotNull(stored);
        assertEquals(TEST_USER_NAME,stored.getUsername());

        TokenResponse response = userService.login(TEST_USER_NAME,TEST_PASSWORD);

        User auth = userService.auth(response.token);

        assertEquals(TEST_USER_NAME,auth.username);

        TokenResponse newTokens = userService.refresh(response.refreshToken);


        auth = userService.auth(newTokens.token);
        assertEquals(TEST_USER_NAME,auth.username);

        assertNull(redisDao.getString(response.refreshToken));
        try{
            userService.refresh(response.refreshToken);

            throw new RuntimeException("false refresh");
        }catch (Exception e){
            auth = userService.get(TEST_USER_NAME);
            assertNull(auth.token);
            assertNull(auth.refreshToken);
        }

        redisDao.delKey(newTokens.token);
        redisDao.delKey(newTokens.refreshToken);

        userService.delete(TEST_USER_NAME);

        assertNull(userService.get(TEST_USER_NAME));
    }

    @Test
    public void deleteTestUser() throws IOException {
        userService.delete(TEST_USER_NAME);

        assertNull(userService.get(TEST_USER_NAME));
    }

    @Test
    public void getNewUsers() throws IOException {
        User user = new User(TEST_USER_NAME,TEST_PASSWORD,new HashSet<>(),true);
        LocalDate date = LocalDate.now();

        user.registered = date.plus(1, ChronoUnit.DAYS);

        userService.create(user);

        List<User> res = userService.getAfter(date);

        assertEquals(1,res.size());

        String newToken = userService.updateReader();

        assertEquals(newToken, userService.getReadersToken());

        userService.delete(TEST_USER_NAME);

        assertNull(userService.get(TEST_USER_NAME));
    }


    @Test
    public void getReader() throws IOException {
        User user = userService.get(userService.getReaderUsername());
        assertNotNull(user);


        System.out.println(user);

        TokenResponse tokens = userService.loginReader();

        assertEquals(tokenService.getReaderTokenLength(),tokens.token.length());

        String token = userService.getReadersToken();

        assertEquals(tokens.token, token);

        TokenResponse newTokens  = userService.loginReader();

        assertNotEquals(token, newTokens.token);

        token = userService.getReadersToken();

        assertEquals(newTokens.token, token);
    }
}
