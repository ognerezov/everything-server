package net.okhotnikov.everything.web;

import net.okhotnikov.everything.api.in.BaseEmailEvent;
import net.okhotnikov.everything.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by Sergey Okhotnikov.
 */
@RestController
@RequestMapping("/service")
public class ServiceController {

    private final UserService userService;
    private static final Logger LOG = LoggerFactory.getLogger(ServiceController.class);

    public ServiceController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/email")
    public void receiveEvents(@RequestBody List<BaseEmailEvent> events){
        for(BaseEmailEvent event:events){
            try {
                userService.setEmailStatus(event.email,event.event,event.reason);
                LOG.info("set email status for user: " + event.email + " : "+event.event);
            }catch (Exception e){
                LOG.error("error setting email status for user: " + event.email + " : "+event.event);
            }
        }
    }
}
