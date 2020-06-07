package net.okhotnikov.everything.web;

import net.okhotnikov.everything.service.UserService;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Created by Sergey Okhotnikov.
 */
@RestController
@RequestMapping("/admin")
public class AdminController {
    private final ConfigurableApplicationContext applicationContext;
    private final UserService userService;

    public AdminController(ConfigurableApplicationContext applicationContext, UserService userService) {
        this.applicationContext = applicationContext;
        this.userService = userService;
    }

    @GetMapping("/close")
    public void closer(){
        applicationContext.close();
    }

    @GetMapping("/change")
    public void changeReadersToken() throws IOException {
        userService.updateReader();
    }
}
