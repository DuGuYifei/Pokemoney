package com.pokemoney.hadoop.service.provider;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * This class is the main class of the hadoop-service.
 */
@SpringBootApplication
@EnableDubbo
@ComponentScan(basePackages = {"com.pokemoney.hadoop.hbase", "com.pokemoney.hadoop.service.provider", "com.pokemoney.commons.config"})
@MapperScan(basePackages = {"com.pokemoney.hadoop.hbase.phoenix"})
public class HadoopServiceApplication {
    /**
     * Application main entry point.
     *
     * @param args The arguments.
     */
    public static void main(String[] args) {
        SpringApplication.run(HadoopServiceApplication.class, args);
    }
}