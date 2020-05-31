package net.okhotnikov.everything.dev;

import net.okhotnikov.everything.api.out.TokenResponse;
import net.okhotnikov.everything.dao.RedisDao;
import net.okhotnikov.everything.model.User;
import net.okhotnikov.everything.service.EmailService;
import net.okhotnikov.everything.service.TokenService;
import net.okhotnikov.everything.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import org.slf4j.Logger;

import static junit.framework.Assert.*;
import static junit.framework.Assert.assertEquals;
import static net.okhotnikov.everything.service.UserServiceTest.TEST_PASSWORD;
import static net.okhotnikov.everything.service.UserServiceTest.TEST_USER_NAME;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by Sergey Okhotnikov.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class DevScripts {

    private final Logger LOG = LoggerFactory.getLogger(DevScripts.class);



    @Autowired
    private EmailService emailService;
    @Autowired
    private UserService userService;

    @Autowired
    private RedisDao redisDao;

    @Autowired
    private TokenService tokenService;

    @Before
    public void  before(){
        LOG.warn("This is development scripts should not be run on build!");
    }

    @Test
    public void processReaderTest() throws IOException {
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

    @Test
    public void test() throws IOException {
        int res = emailService.send("ognerezov@yurnix.tprunu","test");

        assertTrue(res >= 200);
        assertTrue(res < 400);
    }


    @Test
    public void deleteTestUser() throws IOException {
        userService.delete(TEST_USER_NAME);

        assertNull(userService.get(TEST_USER_NAME));
    }

    @Test
    public void printToken() throws IOException {
        System.out.println(userService.getReadersToken());
    }

    @Test
    public void registerTestUser() throws IOException {

        userService.register(TEST_USER_NAME,TEST_PASSWORD);

        System.out.println(userService.get(TEST_USER_NAME));
    }

    @Test
    public void changeReaderToken() throws IOException {

        System.out.println(userService.updateReader());
    }

    @Test
    public void printDefaultUserStatus() throws IOException {
        System.out.println(userService.get("sergey@okhotnikov.net").emailStatus);
    }
}
