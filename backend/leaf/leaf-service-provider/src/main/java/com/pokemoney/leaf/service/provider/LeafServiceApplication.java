package com.pokemoney.leaf.service.provider;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class LeafServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LeafServiceApplication.class, args);
	}
}
