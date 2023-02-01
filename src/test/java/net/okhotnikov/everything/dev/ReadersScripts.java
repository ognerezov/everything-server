package net.okhotnikov.everything.dev;

import net.okhotnikov.everything.model.Role;
import net.okhotnikov.everything.model.User;
import net.okhotnikov.everything.service.EmailService;
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

    @Autowired
    EmailService emailService;

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


    @Test
    public void grantAccess() throws IOException {

        String [] usernames = new String[]{
                "golbez4e9@gmail.com",
                "bmzv07@mail.ru",
                "alikhanislamov666@gmail.com",
                "juliakamel26@gmail.com",
                "freecall0525902116@gmail.com",
                "musurmankulova04@inbox.ru",
                "iconic13@mail.ru",
                "Alibek.mardanov@gmail.com",
                "karina.kiss.2005@rambler.ru",
                "imwe5907@gmail.com"
        };
        for(String username: usernames)
            userService.addRole(username, Role.ROLE_READER);

    }

    @Test
    public void sendEmails() throws IOException {
        String [] usernames = new String[]{
                "golbez4e9@gmail.com",
                "bmzv07@mail.ru",
                "alikhanislamov666@gmail.com",
                "juliakamel26@gmail.com",
                "freecall0525902116@gmail.com",
                "musurmankulova04@inbox.ru",
                "iconic13@mail.ru",
                "Alibek.mardanov@gmail.com",
                "karina.kiss.2005@rambler.ru",
                "imwe5907@gmail.com"
        };
//        String [] usernames = new String[]{
//                "ognerezov@yandex.ru",
//                "ognerezov@gmail.com"
//        };
        for(String username: usernames)
            try {
                emailService.send("sergey@okhotnikov.net", "Вам открыт доступ к everything-from.one", username,
                        "Администрация портала <a href='https://everything-from.one/'> everything-from.one</a> предоставила вам постоянный доступ ко всем материалам. \n" +
                                "<br>" +
                                "В обновленной версии появились числа двадцать первого уровня. От 231 (Риск) до 253 (Законы подлости). Следующие уровни в ближайших планах.");
                System.out.println("Send to: "+username);
            }catch (Exception e){
                System.out.println("Error sending to: "+ username);
            }
    }
}
