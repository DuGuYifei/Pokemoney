package com.pokemoney.hadoop.client.config;

import com.pokemoney.hadoop.client.interceptor.GraphQlRequestHeaderInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GraphQLConfig {

    @Bean
    public GraphQlRequestHeaderInterceptor graphQlRequestHeaderInterceptor() {
        return new GraphQlRequestHeaderInterceptor();
    }
}
