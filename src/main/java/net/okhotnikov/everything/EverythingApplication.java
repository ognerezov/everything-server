package net.okhotnikov.everything;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@PropertySource("file:./application.yaml")
public class EverythingApplication {

    public static void main(String[] args) {
        SpringApplication.run(EverythingApplication.class, args);
    }
}
