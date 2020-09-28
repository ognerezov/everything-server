package net.okhotnikov.everything.dev;

import net.okhotnikov.everything.api.out.TokenResponse;
import net.okhotnikov.everything.dao.ElasticDao;
import net.okhotnikov.everything.dao.RedisDao;
import net.okhotnikov.everything.model.Role;
import net.okhotnikov.everything.model.User;
import net.okhotnikov.everything.service.EmailService;
import net.okhotnikov.everything.service.RedisService;
import net.okhotnikov.everything.service.TokenService;
import net.okhotnikov.everything.service.UserService;
import net.okhotnikov.everything.util.DataUtil;
import net.okhotnikov.everything.web.BookController;
import net.okhotnikov.everything.web.FreeBookController;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

import static junit.framework.Assert.*;
import static junit.framework.Assert.assertEquals;
import static net.okhotnikov.everything.service.ElasticService.USERS;
import static net.okhotnikov.everything.service.UserServiceTest.TEST_PASSWORD;
import static net.okhotnikov.everything.service.UserServiceTest.TEST_USER_NAME;
import static net.okhotnikov.everything.util.Literals.REFRESH_TOKEN;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by Sergey Okhotnikov.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class DevScripts {

    private final Logger LOG = LoggerFactory.getLogger(DevScripts.class);


    @Autowired
    private FreeBookController freeBookController;

    @Autowired
    private BookController bookController;

    @Autowired
    private EmailService emailService;
    @Autowired
    private UserService userService;

    @Autowired
    private RedisDao redisDao;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ElasticDao elasticDao;

    @Value("${reader.username}")
    private String readerUsername;


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
        User user = userService.get(TEST_USER_NAME);
        redisService.delete(user.token);
        userService.delete(TEST_USER_NAME);

        assertNull(userService.get(TEST_USER_NAME));
    }

    @Test
    public void deleteFakeUser() throws IOException {
        userService.delete("Ognerezov");

        assertNull(userService.get("Ognerezov"));
    }

    @Test
    public void printToken() throws IOException {
        System.out.println(userService.getReadersToken());
    }

    @Test
    public void registerTestUser() throws IOException {

        userService.register(TEST_USER_NAME,TEST_PASSWORD);

        getTestUser();
    }

    @Test
    public void getTestUser() throws IOException {
        System.out.println(userService.get(TEST_USER_NAME));
    }

    @Test
    public void changeReaderToken() throws IOException {
        System.out.println(userService.updateReader());
    }

    @Test
    public void printUserStatus() throws IOException {
        System.out.println(userService.get(TEST_USER_NAME).emailStatus);
        System.out.println(userService.get("notemail@notaddress.not").emailStatus);
        System.out.println(userService.get("sergey@okhotnikov.net").emailStatus);
    }

    @Test
    public void getUsersAfter() throws IOException {
        LocalDate date = LocalDate.now().minus(1,ChronoUnit.YEARS);

        List<User> res = userService.getAfter(date);
        System.out.println(res);
        System.out.println(res.size());
    }

    @Test
    public void testAddRole() throws IOException {
        userService.addRole(TEST_USER_NAME, Role.ROLE_READER);
        User stored = userService.get(TEST_USER_NAME);

        assertTrue(stored.roles.contains(Role.ROLE_READER));
    }

    @Test
    public void testRemoveRole() throws IOException {
        userService.removeRole(TEST_USER_NAME, Role.ROLE_READER);
        User stored = userService.get(TEST_USER_NAME);

        assertFalse(stored.roles.contains(Role.ROLE_READER));
    }

    @Test
    public void getUserByToken(){
        System.out.println(redisService.auth("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJldmVyeXRoaW5nIiwiY2xpZW50VHlwZSI6InVzZXIiLCJleHAiOjE1OTM4Njc1NTUsInVzZXJuYW1lIjoib2duZXJlem92QHlhbmRleC5ydSIsInRva2VuX2NyZWF0ZV9kYXRlIjoxNTkxNDQ4MzU1MjUwfQ.q4iuiCQEgL4itGus7LniG6eIvxzj7SOQ0x_qhsT50PbPfJbfgWSnQv00HpFXZWfzHIVHB6J2G3vnEhA3ySATQg"));
    }

    @Test
    public void testLoginReader() throws IOException {
        userService.loginReader();
    }

    @Test
    public void testSearchTest() throws IOException{
        List<Map<String, Object>> res = elasticDao.getWithText("book", "предательств","number", SortOrder.DESC);
        System.out.println(res.size());
        int count =0;
        for (Map<String,Object> map: res){
            System.out.println(map);
            System.out.println("_______________________________________________" + count++ + "_______________________________________________");
        }
    }

    @Test
    public void restoreReadersToken() throws IOException {
        userService.restoreReadersToken();
    }

    @Test
    public void testDateNumbers() throws IOException {
        System.out.println(DataUtil.getDateNumbers(232));
        System.out.println(bookController.getNumberOfTheDay());
    }


    @Test
    public void testGetByField() throws IOException {
        String token = userService.get(TEST_USER_NAME).refreshToken;
        System.out.println(token);
        String [] data = token.split("\\.");
        List<Map<String,Object>> res = elasticDao.getByField(USERS,REFRESH_TOKEN, data[data.length-1],100);
        System.out.println(res.size());
        for(Map<String,Object> map : res)
            System.out.println(map.get(REFRESH_TOKEN));

    }


    @Test
    public void setReaderToken() throws IOException {
        Map<String, Object> data = new HashMap<>();

        data.put("token","i085a");

        elasticDao.update(USERS,readerUsername,data);
        System.out.println(userService.getReadersToken());
    }
}
