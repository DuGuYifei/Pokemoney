package com.pokemoney.leaf.service.provider;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({
		"com.pokemoney.commons.config",
		"com.pokemoney.leaf.service.provider",
})
@EnableDubbo
public class LeafServiceApplication {
	/**
	 * Application main entry point.
	 *
	 * @param args The arguments.
	 */
	public static void main(String[] args) {
		SpringApplication.run(LeafServiceApplication.class, args);
	}
}
