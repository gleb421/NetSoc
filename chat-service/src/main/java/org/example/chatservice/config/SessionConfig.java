package org.example.chatservice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
@EnableRedisHttpSession
public class SessionConfig {
    // Можно оставить пустым, Spring сам подключит RedisTemplate и LettuceConnectionFactory
}
