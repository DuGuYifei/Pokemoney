package com.pokemoney.redisservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * This class is the main class of the redis-service.
 */
@SpringBootApplication
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
