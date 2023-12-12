package com.pokemoney.hadoop.client.controller.graphql;

import com.pokemoney.hadoop.client.exception.GenericGraphQlForbiddenException;
import com.pokemoney.hadoop.client.service.AuthService;
import com.pokemoney.hadoop.client.service.TransactionService;
import com.pokemoney.hadoop.client.service.UserService;
import com.pokemoney.hadoop.hbase.dto.filter.TransactionFilter;
import com.pokemoney.hadoop.hbase.dto.transaction.TransactionDto;
import com.pokemoney.hadoop.hbase.phoenix.model.UserModel;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.sql.SQLException;
import java.util.List;

/**
 * Transaction controller
 */
@Slf4j
@Controller
public class TransactionController {
    /**
     * Transaction service
     */
    private final TransactionService transactionService;

    /**
     * User service
     */
    private final UserService userService;

    /**
     * Auth service
     */
    private final AuthService authService;

    /**
     * Constructor
     *
     * @param transactionService transaction service
     * @param userService        user service
     * @param authService        auth service
     */
    public TransactionController(TransactionService transactionService, UserService userService, AuthService authService) {
        this.transactionService = transactionService;
        this.userService = userService;
        this.authService = authService;
    }

    /**
     * get transaction by transaction id
     *
     * @param transactionId transaction id
     * @param userId        user id
     * @param ledgerId      ledger id
     * @param env           data fetching environment
     * @param auth          auth
     * @return transaction
     * @throws GenericGraphQlForbiddenException generic forbidden error
     */
    @QueryMapping
    public TransactionDto getTransaction(@Argument Long transactionId, @Argument Long userId, @Argument Long ledgerId, DataFetchingEnvironment env, @ContextValue("auth") String auth) throws GenericGraphQlForbiddenException {
//        List<String> selectedFieldsName = authService.preHandleRequest(userId, env, auth);
//        try {
//            UserModel userModel = userService.getUserByUserId(userId);
//            return transactionService.getTransaction(userModel, transactionId, ledgerId, selectedFieldsName);
//        } catch (SQLException e) {
//            log.error("QueryMapping getTransaction error", e);
//            throw new GenericGraphQlForbiddenException("Ledger not found");
//        }
        throw new GenericGraphQlForbiddenException("Not support for user yet");
    }

    /**
     * get transactions by filter. Not support user now.
     *
     * @param filter filter
     * @param userId user id
     * @param ledgerId ledger id
     * @param env data fetching environment
     * @param auth auth
     * @return transactions
     * @throws GenericGraphQlForbiddenException generic forbidden error
     */
    @QueryMapping
    public List<TransactionDto> getTransactions(@Argument TransactionFilter filter, @Argument Long userId, @Argument Long ledgerId, DataFetchingEnvironment env, @ContextValue("auth") String auth) throws GenericGraphQlForbiddenException {
//        List<String> selectedFieldsName = authService.preHandleRequest(userId, env, auth);
//        return transactionService.getTransactionsByFilter(filter, userId, ledgerId);
        throw new GenericGraphQlForbiddenException("Not support for user yet");
    }
}
