package com.pokemoney.hadoop.client.advice;

import com.pokemoney.commons.http.errors.GenericForbiddenError;
import com.pokemoney.hadoop.client.exception.GenericGraphQlForbiddenException;
import com.pokemoney.hadoop.client.exception.GenericGraphQlInternalException;
import graphql.GraphQLError;
import graphql.GraphqlErrorException;
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler;
import org.springframework.graphql.execution.ErrorType;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.sql.SQLException;

/**
 * Advice for Hadoop client graphql
 */
@ControllerAdvice
public class GraphQlGlobalExceptionHandler {
    /**
     * Handle sql exception
     *
     * @param e sql exception
     * @return GraphQLError
     */
    @GraphQlExceptionHandler
    public GraphQLError handle(SQLException e) {
        return GraphQLError.newError().errorType(ErrorType.INTERNAL_ERROR).message("Something went wrong. You can try again and it will be faster this time!").build();
    }

    /**
     * Handle GenericGraphQlForbiddenException
     *
     * @param e GenericGraphQlForbiddenException
     * @return GraphQLError
     */
    @GraphQlExceptionHandler
    public GraphQLError handle(GenericGraphQlForbiddenException e) {
        return GraphQLError.newError().errorType(ErrorType.FORBIDDEN).message(e.getMessage()).build();
    }

    /**
     * Handle GenericGraphQlInternalException
     *
     * @param e GenericGraphQlInternalException
     * @return GraphQLError
     */
    @GraphQlExceptionHandler
    public GraphQLError handle(GenericGraphQlInternalException e) {
        return GraphQLError.newError().errorType(ErrorType.INTERNAL_ERROR).message(e.getMessage()).build();
    }

    /**
     * Handle GraphqlErrorException
     *
     * @param e GraphqlErrorException
     * @return GraphQLError
     */
    @GraphQlExceptionHandler
    public GraphQLError handle(Exception e) {
        return GraphQLError.newError().errorType(ErrorType.INTERNAL_ERROR).message("something went wrong").build();
    }
}
