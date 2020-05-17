package net.okhotnikov.everything.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by Sergey Okhotnikov.
 */
@Configuration
public class RedisConfig {

    @Value("${redis.host}")
    private String host;

    @Bean
    public JedisPool getPool(){
        return new JedisPool(new JedisPoolConfig(), host);
    }
}
