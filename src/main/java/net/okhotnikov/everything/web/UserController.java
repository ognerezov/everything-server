package net.okhotnikov.everything.web;

import net.okhotnikov.everything.api.in.SupportRequest;
import net.okhotnikov.everything.api.in.TokenRequest;
import net.okhotnikov.everything.exceptions.ElasticOperationException;
import net.okhotnikov.everything.exceptions.UnauthorizedException;
import net.okhotnikov.everything.service.EmailService;
import net.okhotnikov.everything.service.PaymentService;
import net.okhotnikov.everything.service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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
    private final UserService userService;
    private final PaymentService paymentService;

    public UserController(EmailService emailService, UserService userService, PaymentService paymentService) {
        this.emailService = emailService;
        this.userService = userService;
        this.paymentService = paymentService;
    }

    @PostMapping("/message")
    public void sendMessageToSupport(@Valid @RequestBody SupportRequest msg) throws IOException {
        emailService.sendFromUser(getCurrentUser().username,SUPPORT_EMAILS_THEME_PREFIX +msg.theme,msg.message);
    }

    @PostMapping("/password")
    public void setPassword(@Valid @RequestBody TokenRequest request){
        try {
            userService.setPassword(getUsernameFromContext(),request.token);
        } catch (IOException e) {
            throw  new ElasticOperationException();
        }
    }

    @PostMapping
    public void purchase(@Valid @RequestBody TokenRequest request){
        try {
            paymentService.appStorePurchase(getUsernameFromContext(),request.token);
        } catch (IOException e) {
            throw  new ElasticOperationException();
        }
    }

    public static String getUsernameFromContext(){
        Object principle  = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principle instanceof UserDetails) {
            UserDetails user = (UserDetails) principle;
            return user.getUsername();
        }
        throw new UnauthorizedException("Unable to get user from context");
    }

}
