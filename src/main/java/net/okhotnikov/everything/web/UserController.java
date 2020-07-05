package net.okhotnikov.everything.web;

import net.okhotnikov.everything.api.in.SupportRequest;
import net.okhotnikov.everything.service.EmailService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

import static net.okhotnikov.everything.EverythingApplication.getCurrentUser;
import static net.okhotnikov.everything.util.Literals.SUPPORT_EMAILS_THEME_PREFIX;

/**
 * Created by Sergey Okhotnikov.
 */
@RestController
@RequestMapping("/usr")
public class UserController {

    private final EmailService emailService;

    public UserController(EmailService emailService) {
        this.emailService = emailService;
    }

    @PostMapping("/message")
    public void sendMessageToSupport(@Valid @RequestBody SupportRequest msg) throws IOException {
        emailService.sendFromUser(getCurrentUser().username,SUPPORT_EMAILS_THEME_PREFIX +msg.theme,msg.message);
    }
}
