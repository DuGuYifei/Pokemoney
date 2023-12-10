package com.pokemoney.hadoop.client.controller.graphql;

import com.pokemoney.hadoop.client.exception.GenericGraphQlForbiddenException;
import com.pokemoney.hadoop.client.exception.GenericGraphQlInternalException;
import com.pokemoney.hadoop.client.service.*;
import com.pokemoney.hadoop.client.vo.*;
import com.pokemoney.hadoop.hbase.dto.category.CategoryDto;
import com.pokemoney.hadoop.hbase.dto.category.SubcategoryDto;
import com.pokemoney.hadoop.hbase.dto.fund.FundDto;
import com.pokemoney.hadoop.hbase.dto.ledger.LedgerDto;
import com.pokemoney.hadoop.hbase.dto.operation.OperationDto;
import com.pokemoney.hadoop.hbase.dto.sync.*;
import com.pokemoney.hadoop.hbase.dto.transaction.TransactionDto;
import com.pokemoney.hadoop.hbase.dto.user.NotificationDto;
import com.pokemoney.hadoop.hbase.dto.user.UpsertUserAppInfoDto;
import com.pokemoney.hadoop.hbase.dto.user.UpsertUserDto;
import com.pokemoney.hadoop.hbase.dto.user.UserDto;
import com.pokemoney.hadoop.hbase.enums.CategoryEnum;
import com.pokemoney.hadoop.hbase.phoenix.model.FundModel;
import com.pokemoney.hadoop.hbase.phoenix.model.OperationModel;
import com.pokemoney.hadoop.hbase.phoenix.model.UserModel;
import com.pokemoney.hadoop.hbase.utils.JsonUtils;
import com.pokemoney.hadoop.hbase.utils.RowKeyUtils;
import com.pokemoney.user.service.api.VerifyUserJwtWithServiceNameResponseDto;
import jdk.dynalink.Operation;
import lombok.extern.slf4j.Slf4j;
import org.apache.phoenix.shaded.org.apache.curator.framework.CuratorFramework;
import org.apache.phoenix.shaded.org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.ContextValue;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Ledger book graphql controller
 */
@Controller
@Slf4j
public class SyncController {
    /**
     * Curator framework
     */
    private final CuratorFramework curatorFramework;
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
     * @param curatorFramework   curator framework
     * @param authService        auth service
     * @param userService        user service
     * @param ledgerService      ledger service
     * @param fundService        fund service
     * @param transactionService transaction service
     * @param subcategoryService subcategory service
     * @param operationService   operation service
     * @param dtpSyncExecutor1   thread pool executor
     */
    public SyncController(CuratorFramework curatorFramework, AuthService authService, UserService userService, LedgerService ledgerService, FundService fundService, TransactionService transactionService, SubcategoryService subcategoryService, OperationService operationService, ThreadPoolExecutor dtpSyncExecutor1) {
        this.curatorFramework = curatorFramework;
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
     * @throws GenericGraphQlInternalException  generic graphql internal exception
     */
    @MutationMapping(name = "syncAll")
    public SyncResponseDto syncAll(
            @Argument Long maxOperationId,
            @Argument SyncUserInputDto user,
            @Argument List<SyncFundInputDto> fund,
            @Argument List<SyncLedgerInputDto> ledger,
            @Argument List<SyncTransactionInputDto> transaction,
            @Argument List<SyncSubcategoryInputDto> subcategory,
            @ContextValue("auth") String auth
    ) throws GenericGraphQlForbiddenException, GenericGraphQlInternalException {
        long userId = user.getUserId();
        // verify auth
        VerifyUserJwtWithServiceNameResponseDto verifiedUserInfo = authService.verifyUser(userId, auth);
        String lockPath = com.pokemoney.hadoop.zookeeper.Constants.ExclusiveOperationMutexPathPrefix + "/" + userId;
        InterProcessMutex lock = new InterProcessMutex(curatorFramework, lockPath);
        try {
            if (lock.acquire(6, TimeUnit.SECONDS)) {
                // get user model from db
                Future<UserModel> userModelFuture = dtpSyncExecutor1.submit(() -> userService.getUserByUserId(userId));
                // get all operations which operationId > maxOperationId
                Future<List<OperationModel>> operationModelListFuture = dtpSyncExecutor1.submit(() -> operationService.getOperationsByOperationId(userId, maxOperationId));
                UserModel userModel;
                List<OperationModel> operationModelList;
                Long returnMaxOperationId = maxOperationId;
                try {
                    userModel = userModelFuture.get();
                    operationModelList = operationModelListFuture.get();
                } catch (Exception e) {
                    log.error("syncAll error when get user model and opt model list. ", e);
                    throw new GenericGraphQlForbiddenException("server is busy, please try later~~");
                }
                if (userModel == null) {
                    log.error("syncAll error when get user. User not exists. userId: {}", userId);
                    throw new GenericGraphQlForbiddenException("User not exists");
                }
                if (operationModelList == null) {
                    log.error("syncAll error when get operations. Operation is empty. userId: {}", userId);
                    throw new GenericGraphQlForbiddenException("Something went wrong, please try later~~");
                }
                // sync subcategory
                Future<List<SubcategoryDto>> subcategoryFuture = dtpSyncExecutor1.submit(() -> subcategoryService.syncSubcategory(subcategory, userModel));
                System.out.println("subcategoryFuture: " + subcategoryFuture);
                DividedOperationLists dividedOperationLists = operationService.divideOperationList(operationModelList);
                if (dividedOperationLists == null) {
                    log.error("syncAll error when divide operation list. userId: {}", userId);
                    throw new GenericGraphQlForbiddenException("Something went wrong, please try later~~");
                }
                if (dividedOperationLists.getFundOperationList().isEmpty()) {
                    System.out.println("fundOperationList is empty");
                } else {
                    System.out.println("fundOperationList is not empty");
                }
                System.out.println("dividedOperationLists: " + dividedOperationLists);
                UserDto userDto = UserDto.fromUserModel(userModel);
                System.out.println("userDto: " + userDto);
                userDto.setEmail(verifiedUserInfo.getEmail());
                System.out.println("userDto email: " + userDto.getEmail());
                userDto.setName(userModel.getUserInfo().getName());
                System.out.println("userDto name: " + userDto.getName());
                Long userInfoUpdateAt = userModel.getUserInfo().getUpdateUserInfoAt();
                if (!userDto.getName().equals(user.getName())) {
                    if (user.getUpdateAt() > userInfoUpdateAt) {
                        userDto.setName(user.getName());
                        userInfoUpdateAt = user.getUpdateAt();
                    }
                }
                System.out.println("userDto name: " + userDto.getName());
                // sync Fund
                Future<ProcessedSyncFunds> fundFuture = dtpSyncExecutor1.submit(() -> fundService.syncFund(fundService.preprocessSyncFund(maxOperationId, fund, dividedOperationLists.getFundOperationList(), userDto), dividedOperationLists.getFundOperationList()));
                // sync Ledger
                Future<ProcessedSyncLedgers> ledgerFuture = dtpSyncExecutor1.submit(() -> ledgerService.syncLedger(ledgerService.preprocessSyncLedger(maxOperationId, ledger, dividedOperationLists.getLedgerOperationList(), userDto), dividedOperationLists.getLedgerOperationList()));
                // update user dto
                System.out.println("wait 2 futures");
                List<SubcategoryDto> subcategoryDtoList;
                List<FundDto> returnSyncFunds;
                List<LedgerDto> returnSyncLedgers;
                try {
                    System.out.println("get subcategoryDtoList futures");
                    subcategoryDtoList = subcategoryFuture.get();
                    System.out.println("get subcategoryDtoList futures done");
                } catch (Exception e) {
                    log.error("syncAll error when get subcategory from future. userId: {}", userId, e);
                    throw new GenericGraphQlInternalException("Something went wrong. You can try again and it will be faster this time!");
                }
                userDto.getAppInfo().setSubcategories(subcategoryDtoList);
                System.out.println("userDto getAppInfo: " + userDto.getAppInfo());
                ProcessedSyncFunds processedSyncFunds;
                try {
                    processedSyncFunds = fundFuture.get();
                    System.out.println("get processedSyncFunds futures");
                    returnSyncFunds = processedSyncFunds.getProcessedSyncFunds();
                    System.out.println("get processedSyncFunds futures done");
                    returnMaxOperationId = Long.max(processedSyncFunds.getMaxOperationId(), returnMaxOperationId);
                    System.out.println("returnMaxOperationId: " + returnMaxOperationId);
                } catch (Exception e) {
                    log.error("syncAll error when get fund from future. userId: {}", userId, e);
                    throw new GenericGraphQlInternalException("Something went wrong. You can try again and it will be faster this time!");
                }
                ProcessedSyncLedgers processedSyncLedgers;
                try {
                    processedSyncLedgers = ledgerFuture.get();
                    System.out.println("get processedSyncLedgers futures");
                    returnSyncLedgers = processedSyncLedgers.getProcessedSyncLedgers();
                    System.out.println("get processedSyncLedgers futures done");
                    returnMaxOperationId = Long.max(processedSyncLedgers.getMaxOperationId(), returnMaxOperationId);
                    System.out.println("returnMaxOperationId: " + returnMaxOperationId);
                    // insert ledger operations
                } catch (Exception e) {
                    log.error("syncAll error when get ledger from future. userId: {}", userId, e);
                    throw new GenericGraphQlInternalException("Something went wrong. You can try again and it will be faster this time!");
                }
                // sync transaction
                Future<ProcessedSyncTransactions> transactionFuture = dtpSyncExecutor1.submit(() -> transactionService.syncTransaction(
                        transactionService.preprocessSyncTransaction(
                                maxOperationId,
                                transaction,
                                dividedOperationLists.getTransactionOperationList(),
                                userDto,
                                processedSyncFunds.getNewFundIdAndSnowflakeIdMap(),
                                processedSyncLedgers.getNewLedgerIdAndSnowflakeIdMap()
                        ),
                        dividedOperationLists.getTransactionOperationList(),
                        userDto,
                        processedSyncFunds.getNewFundIdAndSnowflakeIdMap()
                ));
                System.out.println("wait transactionFuture");
                List<TransactionDto> returnSyncTransactions;
                ProcessedSyncTransactions processedSyncTransactions;

                try {
                    processedSyncTransactions = transactionFuture.get();
                } catch (Exception e) {
                    log.error("syncAll error when get transaction from future. userId: {}", userId, e);
                    throw new GenericGraphQlInternalException("Something went wrong. You can try again and it will be faster this time!");
                }

                System.out.println("get processedSyncTransactions futures");
                returnSyncTransactions = processedSyncTransactions.getProcessedSyncTransactions();
                System.out.println("get processedSyncTransactions futures done");
                returnMaxOperationId = Long.max(processedSyncTransactions.getMaxOperationId(), returnMaxOperationId);
                System.out.println("returnMaxOperationId: " + returnMaxOperationId);
                Map<Long, FundModel> fundModelWithNewBalanceMap = processedSyncTransactions.getFundModels();
                Future<Integer> broadcastFundOperationFuture = dtpSyncExecutor1.submit(() -> fundService.broadcastFundByFundModels(fundModelWithNewBalanceMap.values().stream().toList(), userId));
                System.out.println("fundModelWithNewBalanceMap: " + fundModelWithNewBalanceMap);
                // go through fundModelWithNewBalanceMap, if returnSyncFunds contains fundModelWithNewBalanceMap's fundId, update returnSyncFunds's balance, else add fundModelWithNewBalanceMap to returnSyncFunds
                List<FundModel> returnSyncFundModels = new ArrayList<>();
                System.out.println("returnSyncFunds: " + returnSyncFunds);
                for (Map.Entry<Long, FundModel> entry : fundModelWithNewBalanceMap.entrySet()) {
                    Long fundId = entry.getKey();
                    FundModel fundModel = entry.getValue();
                    boolean isFound = false;
                    for (FundDto fundDto : returnSyncFunds) {
                        if (fundDto.getFundId().equals(fundId)) {
                            fundDto.setBalance(fundModel.getFundInfo().getBalance());
                            isFound = true;
                            break;
                        }
                    }
                    if (!isFound) {
                        returnSyncFundModels.add(fundModel);
                    }
                }

                System.out.println("returnSyncFundModels: " + returnSyncFundModels);
                returnSyncFunds = fundService.addFundModelListToFundDtoListDoUpdateAndBroadcast(returnSyncFundModels, returnSyncFunds);
                System.out.println("returnSyncFunds: " + returnSyncFunds);

                List<CategoryDto> categoryDtoList = CategoryEnum.getInitCategoryDtoList();
                System.out.println("categoryDtoList: " + categoryDtoList);
                UpsertUserDto upsertUserDto = new UpsertUserDto(
                        RowKeyUtils.getRegionId(userId),
                        userDto.getUserId(),
                        userDto.getEmail(),
                        userDto.getName(),
                        userInfoUpdateAt,
                        userDto.getFundInfo(),
                        userDto.getLedgerInfo(),
                        // app info
                        UpsertUserAppInfoDto.builder().jsonCategories(null).jsonSubcategories(JsonUtils.GSON.toJson(subcategoryDtoList)).build(),
                        userDto.getNotification() == null? null : userDto.getNotification().generateJsonString()
                );
                System.out.println("upsertUserDto: " + upsertUserDto);
                userService.updateUser(upsertUserDto);
                System.out.println("updateUser done");

                try {
                    broadcastFundOperationFuture.get();
                } catch (Exception e) {
                    log.error("syncAll error when broadcast fund operation. userId: {}", userId, e);
                    throw new GenericGraphQlInternalException("Something went wrong. You can try again and it will be faster this time!");
                }
                System.out.println("subcategories: " + userDto.getAppInfo().getSubcategories());
                // check null to a list
                return new SyncResponseDto(
                        userDto.checkNullList(),
                        returnSyncFunds != null? returnSyncFunds : List.of(),
                        returnSyncLedgers != null? returnSyncLedgers : new ArrayList<>(),
                        returnSyncTransactions != null? returnSyncTransactions : new ArrayList<>(),
                        categoryDtoList != null? categoryDtoList : new ArrayList<>(),
                        subcategoryDtoList != null? subcategoryDtoList : new ArrayList<>(),
                        userDto.getNotification(),
                        returnMaxOperationId
                );
            }
        } catch (GenericGraphQlForbiddenException e) {
            throw e;
        }catch (Exception e) {
            log.error("syncAll error when acquire lock. userId: {}", userId, e);
        } finally {
            try {
                lock.release();
            } catch (Exception e) {
                log.error("syncAll error when release lock. userId: {}", userId, e);
            }
        }
        throw new GenericGraphQlInternalException("server is busy, please try later~~");
    }
}
