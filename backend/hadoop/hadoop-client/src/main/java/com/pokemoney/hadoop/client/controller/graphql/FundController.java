package com.pokemoney.hadoop.client.controller.graphql;

import com.pokemoney.hadoop.client.exception.GenericGraphQlForbiddenException;
import com.pokemoney.hadoop.client.service.AuthService;
import com.pokemoney.hadoop.client.service.FundService;
import com.pokemoney.hadoop.hbase.dto.fund.FundDto;
import com.pokemoney.hadoop.hbase.dto.filter.FundFilter;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.sql.SQLException;
import java.util.List;

/**
 * Fund  graphql controller
 */
@Controller
@Slf4j
public class FundController {
    /**
     * Fund  service
     */
    private final FundService fundService;

    /**
     * Auth service
     */
    private final AuthService authService;

    /**
     * Constructor
     *
     * @param fundService    fund  service
     * @param authService    auth service
     */
    public FundController(FundService fundService, AuthService authService) {
        this.fundService = fundService;
        this.authService = authService;
    }

    /**
     * Get fund  by fund id.
     *
     * @param fundId fund id
     * @return fund  {@link FundDto}
     * @throws SQLException sql exception
     * @throws GenericGraphQlForbiddenException generic forbidden error
     */
    @QueryMapping
    public FundDto getFund(@Argument Long fundId, @Argument Long userId, DataFetchingEnvironment env, @ContextValue("auth") String auth) throws SQLException, GenericGraphQlForbiddenException {
//        authService.verifyUser(userId, auth);
//        List<String> selectedFieldsName = env.getSelectionSet().getFields()
//                .stream()
//                .map(selectedField -> FundDto.FIELD_NAME_MAPPING.get(selectedField.getName()))
//                .toList();
//        try {
//            return fundService.getFund(fundId, userId, selectedFieldsName);
//        } catch (SQLException e) {
//            log.error("getFund error", e);
//            throw e;
//        }
        throw new GenericGraphQlForbiddenException("Not support for user yet");
    }

    /**
     * Get fund s by filter.
     *
     * @param filter filter {@link FundFilter}
     * @return fund s {@link FundDto}
     * @throws SQLException sql exception
     * @throws GenericGraphQlForbiddenException generic graphql forbidden exception
     */
    @QueryMapping
    public List<FundDto> getFunds(@Argument FundFilter filter, @Argument Long userId, DataFetchingEnvironment env, @ContextValue("auth") String auth) throws SQLException, GenericGraphQlForbiddenException {
//        List<String> selectedFieldsName = authService.preHandleRequest(userId, env, auth);
//        try {
//            return fundService.getFundsByFilter(userId, filter, selectedFieldsName);
//        } catch (SQLException e) {
//            log.error("getFunds error", e);
//            throw e;
//        }
        throw new GenericGraphQlForbiddenException("Not support for user yet");
    }
}
