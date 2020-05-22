package net.okhotnikov.everything.config;

import net.okhotnikov.everything.config.authentication.TokenAuthenticationFilter;
import net.okhotnikov.everything.config.authentication.TokenAuthenticationProvider;
import net.okhotnikov.everything.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
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

    private final TokenAuthenticationProvider tokenAuthenticationProvider;


    private final TokenAuthenticationFilter tokenAuthenticationFilter;

    public SecurityConfig(TokenAuthenticationProvider tokenAuthenticationProvider, TokenAuthenticationFilter tokenAuthenticationFilter) {
        this.tokenAuthenticationProvider = tokenAuthenticationProvider;
        this.tokenAuthenticationFilter = tokenAuthenticationFilter;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/free/**").permitAll()
            .antMatchers("/pub/**").permitAll()
            .antMatchers("/book/**").authenticated();
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
