package com.pokemoney.eurekaserver;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

import java.util.Date;

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
        Date date = new Date();
        System.out.println("Eureka Server Application Start Time: " + date.toString());
        System.out.println("Eureka Server Application Start Time: " + date.getTime());
        SpringApplication.run(EurekaServerApplication.class, args);
    }
}
