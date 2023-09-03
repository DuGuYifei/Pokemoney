package com.pokemoney.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

/**
 * Application main class
 */
@SpringBootApplication
@ComponentScan({"com.pokemoney.userservice", "com.pokemoney.commons.mail", "com.pokemoney.commons.advice", "com.pokemoney.commons.config", "com.pokemoney.commons.errors"})
@EnableFeignClients
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
