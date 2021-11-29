package net.okhotnikov.everything.dev;

import net.okhotnikov.everything.model.User;
import net.okhotnikov.everything.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.time.LocalDateTime;

@SpringBootTest
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
public class ReadersScripts {

    @Autowired
    UserService userService;

    @Test
    public void getReader() throws IOException {
        User user = userService.get(userService.getReaderUsername());
        System.out.println(user);
    }

    @Test
    public void setReadersDate() throws IOException {
        userService.setUpdate(userService.getReaderUsername(), LocalDateTime.now());
    }

    @Test
    public void updateReader() throws IOException {
        System.out.println(userService.updateReader());
    }
}
