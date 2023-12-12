package com.pokemoney.commons.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

/**
 * The configuration of the method validation.
 */
@Configuration
public class MethodValidationConfiguration {

    /**
     * The method validation post processor.
     *
     * @return The method validation post processor.
     */
    @Bean
    public MethodValidationPostProcessor methodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }
}
