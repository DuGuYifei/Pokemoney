package com.pokemoney.hadoop.client.controller.graphql;

import com.pokemoney.hadoop.client.exception.GenericGraphQlForbiddenException;
import com.pokemoney.hadoop.client.service.*;
import com.pokemoney.hadoop.client.vo.DividedOperationLists;
import com.pokemoney.hadoop.client.vo.PreprocessedSyncFunds;
import com.pokemoney.hadoop.client.vo.PreprocessedSyncLedgers;
import com.pokemoney.hadoop.hbase.dto.category.SubcategoryDto;
import com.pokemoney.hadoop.hbase.dto.operation.OperationDto;
import com.pokemoney.hadoop.hbase.dto.sync.*;
import com.pokemoney.hadoop.hbase.dto.user.UserDto;
import com.pokemoney.hadoop.hbase.phoenix.model.OperationModel;
import com.pokemoney.hadoop.hbase.phoenix.model.UserModel;
import com.pokemoney.user.service.api.VerifyUserJwtWithServiceNameResponseDto;
import graphql.schema.DataFetchingEnvironment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Ledger book graphql controller
 */
@Controller
@Slf4j
public class SyncController {
    /**
     * Auth service
     */
    private final AuthService authService;
    /**
     * User service
     */
    private final UserService userService;
    /**
     * Ledger service
     */
    private final LedgerService ledgerService;
    /**
     * Fund service
     */
    private final FundService fundService;
    /**
     * Transaction service
     */
    private final TransactionService transactionService;
    /**
     * Subcategory service
     */
    private final SubcategoryService subcategoryService;
    /**
     * Operation service
     */
    private final OperationService operationService;
    /**
     * Thread pool executor
     */
    private final ThreadPoolExecutor dtpSyncExecutor1;

    /**
     * Constructor
     *
     * @param authService        auth service
     * @param userService        user service
     * @param ledgerService      ledger service
     * @param fundService        fund service
     * @param transactionService transaction service
     * @param subcategoryService subcategory service
     * @param operationService   operation service
     * @param dtpSyncExecutor1   thread pool executor
     */
    public SyncController(AuthService authService, UserService userService, LedgerService ledgerService, FundService fundService, TransactionService transactionService, SubcategoryService subcategoryService, OperationService operationService, ThreadPoolExecutor dtpSyncExecutor1) {
        this.authService = authService;
        this.userService = userService;
        this.ledgerService = ledgerService;
        this.fundService = fundService;
        this.transactionService = transactionService;
        this.subcategoryService = subcategoryService;
        this.operationService = operationService;
        this.dtpSyncExecutor1 = dtpSyncExecutor1;
    }

    /**
     * sync data
     *
     * @param maxOperationId max operation id
     * @param user           user
     * @param fund           funds
     * @param ledger         ledgers
     * @param transaction    transactions
     * @param subcategory    subcategories
     * @return sync response {@link SyncResponseDto}
     * @throws GenericGraphQlForbiddenException generic graphql forbidden exception
     */
    @MutationMapping
    public SyncResponseDto syncAll(
            @Argument Long maxOperationId,
            @Argument SyncUserInputDto user,
            @Argument SyncFundInputDto[] fund,
            @Argument SyncLedgerInputDto[] ledger,
            @Argument SyncTransactionInputDto[] transaction,
            @Argument SyncSubcategoryInputDto[] subcategory,
            DataFetchingEnvironment env,
            @ContextValue("auth") String auth
    ) throws GenericGraphQlForbiddenException {
        long userId = user.getUserId();
        // verify auth
        VerifyUserJwtWithServiceNameResponseDto verifiedUserInfo = authService.verifyUser(userId, auth);
        // get user model from db
        Future<UserModel> userModelFuture = dtpSyncExecutor1.submit(() -> userService.getUserByUserId(userId));
        // get all operations which operationId > maxOperationId
        Future<List<OperationModel>> operationModelLinkedListFuture = dtpSyncExecutor1.submit(() -> operationService.getOperationsByOperationId(userId, maxOperationId));
        UserModel userModel;
        List<OperationModel> operationModelLinkedList;
        try {
            userModel = userModelFuture.get();
            operationModelLinkedList = operationModelLinkedListFuture.get();
        } catch (Exception e) {
            log.error("syncAll error when get user model and opt model list. ", e);
            throw new GenericGraphQlForbiddenException("server is busy, please try later~~");
        }
        if (userModel == null) {
            log.error("syncAll error when get user. User not exists. userId: {}", userId);
            throw new GenericGraphQlForbiddenException("User not exists");
        }
        if (operationModelLinkedList == null) {
            log.error("syncAll error when get operations. Operation is empty. userId: {}", userId);
            throw new GenericGraphQlForbiddenException("Something went wrong, please try later~~");
        }
        // sync subcategory
        Future<Map<String, SubcategoryDto>> subcategoryFuture = dtpSyncExecutor1.submit(() -> subcategoryService.syncSubcategory(subcategory, userModel));
        DividedOperationLists dividedOperationLists = operationService.divideOperationList(operationModelLinkedList);
        UserDto userDto = UserDto.fromUserModel(userModel);
        userDto.setEmail(verifiedUserInfo.getEmail());
        userDto.setName(verifiedUserInfo.getUsername());
        // sync Fund
        Future<PreprocessedSyncFunds> fundFuture = dtpSyncExecutor1.submit(() -> fundService.syncFund(fundService.preprocessSyncFund(maxOperationId, fund, dividedOperationLists.getFundOperationList(), userDto), dividedOperationLists.getFundOperationList()));
        // sync Ledger
        Future<PreprocessedSyncLedgers> ledgerFuture = dtpSyncExecutor1.submit(() -> ledgerService.syncLedger(ledgerService.preprocessSyncLedger(maxOperationId, ledger, dividedOperationLists.getLedgerOperationList(), userDto), dividedOperationLists.getLedgerOperationList()));
        // update user dto
        Map<String, SubcategoryDto> subcategoryDtoMap;
        PreprocessedSyncFunds preprocessedSyncFunds;
        PreprocessedSyncLedgers preprocessedSyncLedgers;
        try {
            subcategoryDtoMap = subcategoryFuture.get();
        } catch (Exception e) {
            log.error("syncAll error when get subcategory from future. userId: {}", userId, e);
            throw new GenericGraphQlForbiddenException("Something went wrong. You can try again and it will be faster this time!");
        }
        try {
            preprocessedSyncFunds = fundFuture.get();
        } catch (Exception e) {
            log.error("syncAll error when get fund from future. userId: {}", userId, e);
            throw new GenericGraphQlForbiddenException("Something went wrong. You can try again and it will be faster this time!");
        }
        try {
            preprocessedSyncLedgers = ledgerFuture.get();
        } catch (Exception e) {
            log.error("syncAll error when get ledger from future. userId: {}", userId, e);
            throw new GenericGraphQlForbiddenException("Something went wrong. You can try again and it will be faster this time!");
        }
        // insert operation
        Future<Integer> operationFuture = dtpSyncExecutor1.submit(() -> operationService.insertOperations(new List[]{
                preprocessedSyncFunds.getUpdateFundOperationDtoList(),
                preprocessedSyncFunds.getInsertFundOperationDtoList(),
                preprocessedSyncLedgers.getUpdateLedgerOperationDtoList(),
                preprocessedSyncLedgers.getInsertLedgerOperationDtoList()}
        ));
        // insert update to other editor


        userDto.getAppInfo().setSubcategories(subcategoryDtoMap.values().stream().toList());

        // 遍历ledger时候，如果有operation包含一样的ledgerId,且更新时间大于ledger，则更新ledger
        // 遍历subcategory时候，如果有operation包含一样的subcategoryId,且更新时间大于subcategory，则更新subcategory
        // 如果有更新就把新的subcategory json 化放到user table的subcategory字段
        // 遍历transaction时候，如果有operation包含一样的transactionId,且更新时间大于transaction，则更新transaction
        //
        return null;
    }
}
