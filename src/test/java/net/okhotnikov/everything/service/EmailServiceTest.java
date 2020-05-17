package net.okhotnikov.everything.service;

import static junit.framework.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

/**
 * Created by Sergey Okhotnikov.
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Test
    public void test() throws IOException {
        int res = emailService.send(emailService.getAdminEmail(),"test",emailService.getRootEmail(),"test body");

        assertTrue(res >= 200);
        assertTrue(res < 400);
    }
}
