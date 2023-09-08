package com.pokemoney.redisservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * This class is the main class of the redis-service.
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.pokemoney.redisservice", "com.pokemoney.commons.http", "com.pokemoney.commons.config", "com.pokemoney.commons.redis*"})
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
