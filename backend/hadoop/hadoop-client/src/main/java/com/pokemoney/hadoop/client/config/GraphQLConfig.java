package com.pokemoney.hadoop.client.config;

import com.pokemoney.hadoop.client.interceptor.GraphQlRequestHeaderInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * GraphQL config
 */
@Configuration
public class GraphQLConfig {
    /**
     * GraphQlRequestHeaderInterceptor
     *
     * @return GraphQlRequestHeaderInterceptor
     */
    @Bean
    public GraphQlRequestHeaderInterceptor graphQlRequestHeaderInterceptor() {
        return new GraphQlRequestHeaderInterceptor();
    }
}
