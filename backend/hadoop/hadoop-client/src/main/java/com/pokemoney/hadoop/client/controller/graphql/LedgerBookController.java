package com.pokemoney.hadoop.client.controller.graphql;

import com.pokemoney.hadoop.client.exception.GenericGraphQlForbiddenException;
import com.pokemoney.hadoop.client.service.AuthService;
import com.pokemoney.hadoop.client.service.LedgerService;
import com.pokemoney.hadoop.hbase.dto.fund.FundDto;
import com.pokemoney.hadoop.hbase.dto.ledger.LedgerDto;
import com.pokemoney.hadoop.hbase.dto.filter.LedgerFilter;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.sql.SQLException;
import java.util.List;

/**
 * Ledger book graphql controller
 */
@Controller
@Slf4j
public class LedgerBookController {
    /**
     * Ledger book service
     */
    private final LedgerService ledgerService;

    /**
     * Auth service
     */
    private final AuthService authService;

    /**
     * Constructor
     *
     * @param ledgerService  ledger book service
     * @param authService    auth service
     */
    public LedgerBookController(LedgerService ledgerService, AuthService authService) {
        this.ledgerService = ledgerService;
        this.authService = authService;
    }

    /**
     * Get ledger book by ledger id.
     *
     * @param ledgerId ledger id
     * @return ledger book {@link LedgerDto}
     * @throws SQLException sql exception
     * @throws GenericGraphQlForbiddenException generic forbidden error
     */
    @QueryMapping
    public LedgerDto getLedger(@Argument Long ledgerId, @Argument Long userId, DataFetchingEnvironment env, @ContextValue("auth") String auth) throws SQLException, GenericGraphQlForbiddenException {
//        authService.verifyUser(userId, auth);
//        List<String> selectedFieldsName = env.getSelectionSet().getFields()
//                .stream()
//                .map(selectedField -> LedgerDto.FIELD_NAME_MAPPING.get(selectedField.getName()))
//                .toList();
//        try {
//            return ledgerService.getLedger(ledgerId, userId, selectedFieldsName);
//        } catch (SQLException e) {
//            log.error("getLedger error", e);
//            throw e;
//        }
        throw new GenericGraphQlForbiddenException("You are not authorized to access this resource");
    }

    /**
     * Get ledger books by filter.
     *
     * @param filter filter {@link LedgerFilter}
     * @return ledger books {@link LedgerDto}
     * @throws SQLException sql exception
     * @throws GenericGraphQlForbiddenException generic forbidden error
     */
    @QueryMapping
    public List<LedgerDto> getLedgers(@Argument LedgerFilter filter, @Argument Long userId, DataFetchingEnvironment env, @ContextValue("auth") String auth) throws SQLException, GenericGraphQlForbiddenException {
//        List<String> selectedFieldsName = authService.preHandleRequest(userId, env, auth);
//        try {
//            return ledgerService.getLedgersByFilter(userId, filter, selectedFieldsName);
//        } catch (SQLException e) {
//            log.error("getLedgers error", e);
//            throw e;
//        }
        throw new GenericGraphQlForbiddenException("Not support for user yet");
    }
}
