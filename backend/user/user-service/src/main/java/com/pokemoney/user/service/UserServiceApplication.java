package com.pokemoney.user.service;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Application main class
 */
@SpringBootApplication
@ComponentScan({"com.pokemoney.user.service",
        "com.pokemoney.commons.mail",
        "com.pokemoney.commons.http",
        "com.pokemoney.commons.config",
        "com.pokemoney.redis.service.api",
        "com.pokemoney.leaf.service.api"
})
@EnableDubbo
public class UserServiceApplication {
    /**
     * Application main entry point.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
