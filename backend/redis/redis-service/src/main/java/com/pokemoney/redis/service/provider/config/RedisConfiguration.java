package com.pokemoney.redis.service.provider.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

/**
 * This class contains all the configurations about the redis.
 */
@Configuration
public class RedisConfiguration {

    /**
     * The bean of the redis template for <String, Object>.
     *
     * @param connectionFactory The redis connection factory.
     * @return The redis template for <String, Object>.
     */
    @Bean
    public RedisTemplate<String, Object> userRedisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);

        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(RedisSerializer.string());

        return redisTemplate;
    }
}
