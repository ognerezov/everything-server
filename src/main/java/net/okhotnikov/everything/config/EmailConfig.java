package net.okhotnikov.everything.config;

import com.sendgrid.SendGrid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Sergey Okhotnikov.
 */
@Configuration
public class EmailConfig {

    @Value("${mail.key}")
    private String key;

    @Bean
    public SendGrid getSendGrid(){
        return new SendGrid(key);
    }
}
