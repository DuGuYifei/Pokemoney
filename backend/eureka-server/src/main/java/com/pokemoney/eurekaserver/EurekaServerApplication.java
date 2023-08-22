package com.pokemoney.eurekaserver;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Application main class
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {
    /**
     * Application main entry point.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}
