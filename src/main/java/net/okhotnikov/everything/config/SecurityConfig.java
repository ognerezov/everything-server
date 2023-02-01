package net.okhotnikov.everything.config;

import net.okhotnikov.everything.config.authentication.TokenAuthenticationFilter;
import net.okhotnikov.everything.config.cors.CorsFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;

/**
 * Created by Sergey Okhotnikov.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig  extends WebSecurityConfigurerAdapter {

    private final TokenAuthenticationFilter tokenAuthenticationFilter;
    private final CorsFilter corsFilter;

    public SecurityConfig(TokenAuthenticationFilter tokenAuthenticationFilter, CorsFilter corsFilter) {
        this.tokenAuthenticationFilter = tokenAuthenticationFilter;
        this.corsFilter = corsFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class);
        http
            .addFilterBefore(tokenAuthenticationFilter,UsernamePasswordAuthenticationFilter.class)
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/free/**").permitAll()
            .antMatchers("/pub/**").permitAll()
            .antMatchers("/service/**").permitAll()
            .antMatchers("/book/**").permitAll()
            .antMatchers("/usr/**").hasRole("USER")
            .antMatchers("/admin/**").hasRole("ADMIN");
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
        return new DefaultHttpFirewall();
    }

}
