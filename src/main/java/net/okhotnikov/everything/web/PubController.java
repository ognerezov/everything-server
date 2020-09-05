package net.okhotnikov.everything.web;

import net.okhotnikov.everything.api.in.RegisterRequest;
import net.okhotnikov.everything.api.in.SupportRequest;
import net.okhotnikov.everything.api.in.TokenRequest;
import net.okhotnikov.everything.api.out.RegisterResponse;
import net.okhotnikov.everything.api.out.TokenResponse;
import net.okhotnikov.everything.exceptions.ElasticOperationException;
import net.okhotnikov.everything.exceptions.NotFoundException;
import net.okhotnikov.everything.model.User;
import net.okhotnikov.everything.service.EmailService;
import net.okhotnikov.everything.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;

/**
 * Created by Sergey Okhotnikov.
 */
@RestController
@RequestMapping("/pub")
public class PubController {

    private final UserService userService;

    public PubController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public RegisterResponse register(@Valid @RequestBody RegisterRequest registerRequest){
        try {
            return userService.register(registerRequest.username,registerRequest.password);
        } catch (IOException e) {
            throw new ElasticOperationException();
        }
    }

    @PutMapping("/login")
    public TokenResponse login(@Valid @RequestBody RegisterRequest registerRequest){
        try {
            return userService.login(registerRequest.username,registerRequest.password);
        } catch (IOException e) {
            throw new ElasticOperationException();
        }
    }

    @PutMapping("/refresh")
    public TokenResponse refresh(@Valid @RequestBody TokenRequest request){
        try {
            return userService.refresh(request.token);
        } catch (IOException e) {
            throw new ElasticOperationException();
        }
    }

    @GetMapping("/forget/{id:.+}")
    public void forgetPassword(@PathVariable String id){
        try {
            User user = userService.get(id);
            if (user == null){
                throw new NotFoundException();
            }

            userService.setTemporalCode(user);

        } catch (IOException e) {
            throw new ElasticOperationException();
        }
    }
}
