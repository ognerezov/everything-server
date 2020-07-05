package net.okhotnikov.everything;


import net.okhotnikov.everything.model.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.core.context.SecurityContextHolder;

@SpringBootApplication
@PropertySource("file:./application.yaml")
public class EverythingApplication {

    public static void main(String[] args) {
        SpringApplication.run(EverythingApplication.class, args);
    }

    public static User getCurrentUser(){
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
