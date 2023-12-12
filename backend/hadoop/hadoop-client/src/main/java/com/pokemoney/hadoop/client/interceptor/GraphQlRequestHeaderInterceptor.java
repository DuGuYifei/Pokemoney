package com.pokemoney.hadoop.client.interceptor;

import com.pokemoney.hadoop.client.Constants;
import lombok.NonNull;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import reactor.core.publisher.Mono;

import java.util.Collections;

/**
 * GraphQlRequestHeaderInterceptor
 */
public class GraphQlRequestHeaderInterceptor implements WebGraphQlInterceptor {
    /**
     * Intercept for auth header
     *
     * @param request request
     * @param chain   chain
     * @return Mono<WebGraphQlResponse>
     */
    @Override
    public @NonNull Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, @NonNull Chain chain) {
        String value = request.getHeaders().getFirst("Authorization");
        if (value == null) {
            value = "";
        }
        String finalValue = value;
        request.configureExecutionInput((executionInput, builder) ->
                builder.graphQLContext(Collections.singletonMap("auth", finalValue)).build());
        return chain.next(request);
    }
}
