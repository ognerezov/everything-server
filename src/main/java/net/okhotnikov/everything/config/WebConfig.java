package net.okhotnikov.everything.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created by Sergey Okhotnikov.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("PUT", "DELETE","GET","POST","OPTIONS")
                .allowCredentials(true)
                .allowedHeaders("*")
                .allowedOrigins("*")
                .maxAge(3600);
    }
}
