package com.pokemoney.hadoop.client;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The hadoop client application.
 */
@SpringBootApplication
public class HadoopClientApplication {
    /**
     * The main entry point.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(HadoopClientApplication.class, args);
    }
}