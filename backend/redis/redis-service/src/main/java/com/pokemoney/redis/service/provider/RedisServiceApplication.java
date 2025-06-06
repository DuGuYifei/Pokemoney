package com.pokemoney.redis.service.provider;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * This class is the main class of the redis-service.
 */
@SpringBootApplication
@EnableDubbo
@ComponentScan(basePackages = {"com.pokemoney.redis.service", "com.pokemoney.commons.config"})
public class RedisServiceApplication {
    /**
     * Application main entry point.
     *
     * @param args The arguments.
     */
    public static void main(String[] args) {
        SpringApplication.run(RedisServiceApplication.class, args);
    }
}
