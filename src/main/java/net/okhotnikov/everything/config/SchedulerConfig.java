package net.okhotnikov.everything.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * Created by Sergey Okhotnikov.
 */
@Configuration
public class SchedulerConfig {

    @Bean("scheduler")
    public ScheduledThreadPoolExecutor getScheduler(){
        return new ScheduledThreadPoolExecutor(2);
    }
}
