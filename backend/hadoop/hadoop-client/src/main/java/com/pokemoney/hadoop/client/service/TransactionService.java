package com.pokemoney.hadoop.client.service;

import com.pokemoney.hadoop.client.Constants;
import com.pokemoney.hadoop.client.exception.GenericGraphQlForbiddenException;
import com.pokemoney.hadoop.client.kafka.KafkaService;
import com.pokemoney.hadoop.client.vo.PreprocessedSyncTransactions;
import com.pokemoney.hadoop.client.vo.ProcessedSyncTransactions;
import com.pokemoney.hadoop.hbase.dto.filter.TransactionFilter;
import com.pokemoney.hadoop.hbase.dto.transaction.TransactionDto;
import com.pokemoney.hadoop.hbase.dto.transaction.UpsertTransactionDto;
import com.pokemoney.hadoop.hbase.dto.operation.OperationDto;
import com.pokemoney.hadoop.hbase.dto.sync.SyncTransactionInputDto;
import com.pokemoney.hadoop.hbase.dto.user.UserDto;
import com.pokemoney.hadoop.hbase.enums.TransactionTypeEnum;
import com.pokemoney.hadoop.hbase.phoenix.dao.FundMapper;
import com.pokemoney.hadoop.hbase.phoenix.dao.LedgerMapper;
import com.pokemoney.hadoop.hbase.phoenix.dao.TransactionMapper;
import com.pokemoney.hadoop.hbase.phoenix.model.*;
import com.pokemoney.hadoop.hbase.utils.RowKeyUtils;
import com.pokemoney.hadoop.hbase.utils.TransactionUtils;
import com.pokemoney.leaf.service.api.LeafGetRequestDto;
import com.pokemoney.leaf.service.api.LeafTriService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Transaction service
 */
@Slf4j
@Service
public class TransactionService {
    /**
     * The kafka service.
     */
    private final KafkaService kafkaService;
    /**
     * The transaction mapper.
     */
    private final TransactionMapper transactionMapper;

    /**
     * The ledger mapper
     */
    private final LedgerMapper ledgerMapper;

    /**
     * The fund mapper
     */
    private final FundMapper fundMapper;

    /**
     * The sql session factory.
     */
    private final SqlSessionFactory sqlSessionFactory;

    /**
     * The dynamic thread pool executor 1.
     */
    private final ThreadPoolExecutor dtpSyncExecutor1;

    /**
     * leaf triple api
     */
    @DubboReference(version = "1.0.0", protocol = "tri", group = "leaf", timeout = 10000)
    private final LeafTriService leafTriService;

    /**
     * Instantiates a new Transaction  service.
     *
     * @param transactionMapper the transaction mapper
     * @param ledgerMapper      the ledger mapper
     * @param fundMapper        the fund mapper
     * @param sqlSessionFactory the sql session factory
     * @param dtpSyncExecutor1  the dynamic thread pool executor 1
     * @param leafTriService    leaf api
     * @param kafkaService      kafka service
     */
    public TransactionService(TransactionMapper transactionMapper, LedgerMapper ledgerMapper, FundMapper fundMapper, SqlSessionFactory sqlSessionFactory, ThreadPoolExecutor dtpSyncExecutor1, LeafTriService leafTriService, KafkaService kafkaService) {
        this.transactionMapper = transactionMapper;
        this.ledgerMapper = ledgerMapper;
        this.fundMapper = fundMapper;
        this.sqlSessionFactory = sqlSessionFactory;
        this.dtpSyncExecutor1 = dtpSyncExecutor1;
        this.leafTriService = leafTriService;
        this.kafkaService = kafkaService;
    }

    /**
     * Preprocess sync transaction.
     *
     * @param syncOperationId sync operation id
     * @param syncTransactionInputDtoList sync transaction input dto list {@link SyncTransactionInputDto}
     * @param operationModelTargetTransactionList operation model target transaction list {@link OperationModel}
     * @param userDto user dto {@link UserDto}
     * @return the preprocessed sync transactions {@link PreprocessedSyncTransactions}
     */
    public PreprocessedSyncTransactions preprocessSyncTransaction(long syncOperationId, List<SyncTransactionInputDto> syncTransactionInputDtoList, LinkedList<OperationModel> operationModelTargetTransactionList, UserDto userDto) {
        PreprocessedSyncTransactions preprocessedSyncTransactions = new PreprocessedSyncTransactions(syncOperationId);
        List<UpsertTransactionDto> updateTransactionDtoList = preprocessedSyncTransactions.getUpdateTransactionDtoList();
        List<UpsertTransactionDto> insertTransactionDtoList = preprocessedSyncTransactions.getInsertTransactionDtoList();
        List<TransactionDto> returnTransactionDtoList = preprocessedSyncTransactions.getReturnTransactionDtoList();
        List<OperationDto> updateTransactionOperationDtoList = preprocessedSyncTransactions.getUpdateTransactionOperationDtoList();
        List<OperationDto> insertTransactionOperationDtoList = preprocessedSyncTransactions.getInsertTransactionOperationDtoList();
        List<SyncTransactionInputDto> noPermissionUpdateTransactionInputDtoList = preprocessedSyncTransactions.getNoPermissionUpdateTransactionInputDtoList();
        Long operationId = preprocessedSyncTransactions.getCurOperationId();
        Map<Long, LedgerModel> ledgerIdToModelMap = preprocessedSyncTransactions.getLedgerIdToModelMap();
        Map<Long, FundModel> fundIdToModelMap = preprocessedSyncTransactions.getFundIdToModelMap();
        // if transaction in the sync already exist in un-sync option, and update date is bigger than the un-sync one, then use new one
        List<Long> fundIds = userDto.getFundInfo().getFunds().stream().map(num -> Long.parseLong(num.split(com.pokemoney.hadoop.hbase.Constants.ROW_KEY_DELIMITER)[2])).toList();
        List<Long> ledgerIds = userDto.getLedgerInfo().getLedgers().stream().map(num -> Long.parseLong(num.split(com.pokemoney.hadoop.hbase.Constants.ROW_KEY_DELIMITER)[2])).toList();
        for (SyncTransactionInputDto syncTransactionInputDto : syncTransactionInputDtoList) {
            boolean isExist = false;
            Long fundIdOfSyncTransaction = syncTransactionInputDto.getFundId();
            int fundInListIndex = fundIds.indexOf(fundIdOfSyncTransaction);
            if (fundInListIndex == -1) {
                noPermissionUpdateTransactionInputDtoList.add(syncTransactionInputDto);
                continue;
            }
            int ledgerInListIndex = ledgerIds.indexOf(syncTransactionInputDto.getLedgerId());
            if (ledgerInListIndex == -1) {
                noPermissionUpdateTransactionInputDtoList.add(syncTransactionInputDto);
                continue;
            }
            FundModel fundModel = fundIdToModelMap.get(fundIdOfSyncTransaction);
            if (fundModel == null) {
                String[] fundRowKeySplit = userDto.getFundInfo().getFunds().get(fundInListIndex).split(com.pokemoney.hadoop.hbase.Constants.ROW_KEY_DELIMITER);
                Integer fundRegionId = Integer.parseInt(fundRowKeySplit[0]);
                Long fundOwnerId = Long.parseLong(fundRowKeySplit[1]);
                fundModel = fundMapper.getFundByRowKey(fundRegionId, fundOwnerId, fundIdOfSyncTransaction, null);
                fundIdToModelMap.put(fundIdOfSyncTransaction, fundModel);
            }
            LedgerModel ledgerModel = ledgerIdToModelMap.get(syncTransactionInputDto.getLedgerId());
            if (ledgerModel == null) {
                String[] ledgerRowKeySplit = userDto.getLedgerInfo().getLedgers().get(ledgerInListIndex).split(com.pokemoney.hadoop.hbase.Constants.ROW_KEY_DELIMITER);
                Integer ledgerRegionId = Integer.parseInt(ledgerRowKeySplit[0]);
                Long ledgerOwnerId = Long.parseLong(ledgerRowKeySplit[1]);
                ledgerModel = ledgerMapper.getLedgerByRowKey(ledgerRegionId, ledgerOwnerId, syncTransactionInputDto.getLedgerId(), null);
                ledgerIdToModelMap.put(syncTransactionInputDto.getLedgerId(), ledgerModel);
            }
            Iterator<OperationModel> operationModelIterator = operationModelTargetTransactionList.iterator();
            Long ownerId = ledgerModel.getLedgerInfo().getOwner();
            Long userId = userDto.getUserId();
            Integer transactionRegionId = ledgerModel.getRegionId();
            Integer userRegionId = RowKeyUtils.getRegionId(userId);
            while (operationModelIterator.hasNext()) {
                OperationModel operationModel = operationModelIterator.next();
                if (operationModel.getOperationInfo().getId().equals(syncTransactionInputDto.getTransactionId())) {
                    isExist = true;
                    if (operationModel.getUpdateAt() < syncTransactionInputDto.getUpdateAt()) {
                        operationModelIterator.remove();
                        Long transactionId = syncTransactionInputDto.getTransactionId();
                        String tableName = TransactionUtils.GetTableNameFromSnowflakeId(transactionId);
                        preprocessSyncUpsertSituation(updateTransactionDtoList, syncTransactionInputDto, tableName, ownerId, userId, transactionId, transactionRegionId);
                        // insert operation to user who write the transaction
                        operationId = Long.parseLong(leafTriService.getSnowflakeId(LeafGetRequestDto.newBuilder().setKey(com.pokemoney.hadoop.hbase.Constants.LEAF_HBASE_OPERATION).build()).getId());
                        updateTransactionOperationDtoList.add(new OperationDto(
                                userRegionId,
                                userId,
                                Long.MAX_VALUE - operationId,
                                operationId,
                                tableName,
                                RowKeyUtils.getTransactionRowKey(transactionRegionId.toString(), userId.toString(), ledgerIds.toString(), ((Long)(Long.MAX_VALUE - transactionId)).toString()),
                                syncTransactionInputDto.getUpdateAt()
                        ));
                    }
                    break;
                }
            }
            if (!isExist) {
                Long transactionId = syncTransactionInputDto.getTransactionId();
                operationId = Long.parseLong(leafTriService.getSnowflakeId(LeafGetRequestDto.newBuilder().setKey(com.pokemoney.hadoop.hbase.Constants.LEAF_HBASE_OPERATION).build()).getId());
                // insert to self
                if (transactionId < com.pokemoney.hadoop.hbase.Constants.MIN_SNOWFLAKE_ID) {
                    transactionId = Long.parseLong(leafTriService.getSnowflakeId(LeafGetRequestDto.newBuilder().setKey(com.pokemoney.hadoop.hbase.Constants.LEAF_HBASE_TRANSACTION).build()).getId());
                    String tableName = TransactionUtils.GetTableNameFromSnowflakeId(transactionId);
                    preprocessSyncUpsertSituation(insertTransactionDtoList, syncTransactionInputDto, tableName, ownerId, userId, transactionId, transactionRegionId);
                    insertTransactionOperationDtoList.add(new OperationDto(
                            userRegionId,
                            userId,
                            Long.MAX_VALUE - operationId,
                            operationId,
                            tableName,
                            RowKeyUtils.getTransactionRowKey(transactionRegionId.toString(), userId.toString(), ledgerIds.toString(), ((Long)(Long.MAX_VALUE - transactionId)).toString()),
                            syncTransactionInputDto.getUpdateAt()
                    ));
                    returnTransactionDtoList.add(new TransactionDto(
                            transactionId,
                            syncTransactionInputDto.getMoney(),
                            syncTransactionInputDto.getTypeId(),
                            syncTransactionInputDto.getRelevantEntity(),
                            syncTransactionInputDto.getComment(),
                            syncTransactionInputDto.getFundId(),
                            syncTransactionInputDto.getCategoryId(),
                            syncTransactionInputDto.getSubcategoryId(),
                            syncTransactionInputDto.getLedgerId(),
                            syncTransactionInputDto.getHappenAt(),
                            userId,
                            syncTransactionInputDto.getUpdateAt(),
                            syncTransactionInputDto.getDelFlag()
                    ));
                } else {
                    String tableName = TransactionUtils.GetTableNameFromSnowflakeId(transactionId);
                    preprocessSyncUpsertSituation(updateTransactionDtoList, syncTransactionInputDto, tableName, ownerId, userId, transactionId, transactionRegionId);
                    updateTransactionOperationDtoList.add(new OperationDto(
                            userRegionId,
                            userId,
                            Long.MAX_VALUE - operationId,
                            operationId,
                            tableName,
                            RowKeyUtils.getTransactionRowKey(transactionRegionId.toString(), ownerId.toString(), ledgerModel.getLedgerId().toString(), transactionId.toString()),
                            syncTransactionInputDto.getUpdateAt()
                    ));
                }
            }
        }
        preprocessedSyncTransactions.setCurOperationId(operationId);
        return preprocessedSyncTransactions;
    }

    /**
     * Preprocess sync upsert situation.
     *
     * @param updateOrInsertTransactionDtoList  update transaction dto list
     * @param syncTransactionInputDto           sync transaction input dto
     * @param tableName                         table name
     * @param ownerId                           owner id
     * @param userId                            user id
     * @param transactionId                     transaction id
     * @param transactionRegionId               region id
     */
    private void preprocessSyncUpsertSituation(List<UpsertTransactionDto> updateOrInsertTransactionDtoList, SyncTransactionInputDto syncTransactionInputDto, String tableName, Long ownerId, Long userId, Long transactionId, Integer transactionRegionId) {
        updateOrInsertTransactionDtoList.add(UpsertTransactionDto.builder()
                .tableName(tableName)
                .regionId(transactionRegionId)
                .userId(ownerId)
                .transactionId(transactionId)
                .money(syncTransactionInputDto.getMoney())
                .typeId(syncTransactionInputDto.getTypeId())
                .relevantEntity(syncTransactionInputDto.getRelevantEntity())
                .comment(syncTransactionInputDto.getComment())
                .fundId(syncTransactionInputDto.getFundId())
                .categoryId(syncTransactionInputDto.getCategoryId())
                .subcategoryId(syncTransactionInputDto.getSubcategoryId())
                .ledgerId(syncTransactionInputDto.getLedgerId())
                .happenAt(syncTransactionInputDto.getHappenAt())
                .updateBy(userId)
                .updateAt(syncTransactionInputDto.getUpdateAt())
                .delFlag(syncTransactionInputDto.getDelFlag())
                .build());
    }

    /**
     * Sync transaction.
     *
     * @param preprocessedSyncTransactions  preprocessed sync transactions {@link PreprocessedSyncTransactions}
     * @param operationModelTargetTransactionList  operation model target transaction list {@link OperationModel}
     * @param userDto user dto {@link UserDto}
     * @return the processed sync transactions {@link ProcessedSyncTransactions}
     * @throws SQLException sql exception
     */
    public ProcessedSyncTransactions syncTransaction (PreprocessedSyncTransactions preprocessedSyncTransactions, LinkedList<OperationModel> operationModelTargetTransactionList, UserDto userDto) throws SQLException {
        Future<Integer> insertFuture = dtpSyncExecutor1.submit(() -> {
            try {
                return insertNewTransaction(preprocessedSyncTransactions.getInsertTransactionDtoList(), preprocessedSyncTransactions.getFundIdToModelMap());
            } catch (SQLException e) {
                log.error("insertNewTransaction error", e);
                throw e;
            }
        });
        Future<List<TransactionDto>> updateFuture = dtpSyncExecutor1.submit(() -> {
            try(SqlSession session = sqlSessionFactory.openSession(false)) {
                try {
                    updateTransactionsByRowKey(preprocessedSyncTransactions.getUpdateTransactionDtoList(), preprocessedSyncTransactions.getFundIdToModelMap());
                    session.commit();
                    Future<List<TransactionDto>> transactionDtoFromUpdateOperationDtoFuture = dtpSyncExecutor1.submit(() -> getTransactionsByUpdateOperationDtoListAndBroadcastToEditors(preprocessedSyncTransactions.getUpdateTransactionOperationDtoList(), preprocessedSyncTransactions.getLedgerIdToModelMap(), preprocessedSyncTransactions.getFundIdToModelMap(), userDto.getUserId()));
                    preprocessedSyncTransactions.getReturnTransactionDtoList().addAll(getTransactionsByOperationModelList(operationModelTargetTransactionList));
                    try {
                        preprocessedSyncTransactions.getReturnTransactionDtoList().addAll(transactionDtoFromUpdateOperationDtoFuture.get());
                    } catch (Exception e) {
                        log.error("extract from getTransactionsByUpdateOperationDtoListAndBroadcastToEditors error", e);
                        throw new SQLException(e);
                    }
                    return preprocessedSyncTransactions.getReturnTransactionDtoList();
                } catch (SQLException e) {
                    log.error("updateTransactionByRowKey or broadcast error", e);
                    session.rollback();
                    throw new RuntimeException(e);
                }
            }
        });
        try {
            insertFuture.get();
            return new ProcessedSyncTransactions(
                    updateFuture.get(),
                    preprocessedSyncTransactions.getNoPermissionUpdateTransactionInputDtoList(),
                    preprocessedSyncTransactions.getFundIdToModelMap(),
                    preprocessedSyncTransactions.getCurOperationId()
            );
        } catch (Exception e) {
            log.error("syncTransaction error", e);
            throw new SQLException(e);
        }
    }

    /**
     * Get transactions by operation list.
     *
     * @param operationModelList operation model list
     * @return transaction dto list
     * @throws SQLException sql exception
     */
    public List<TransactionDto> getTransactionsByOperationModelList(List<OperationModel> operationModelList) throws SQLException {
        List<TransactionDto> transactionDtoList = new ArrayList<>();
        try {
            for (OperationModel operationModel : operationModelList) {
                transactionDtoList.add(selectTransactionDtoByRowKeyAndTableName(operationModel.getOperationInfo().getTargetRowKey(), operationModel.getOperationInfo().getTargetTable()));
            }
        } catch (Exception e) {
            log.error("getTransactionsByOperationList error", e);
            throw new SQLException(e);
        }
        return transactionDtoList;
    }

    /**
     * Get transactions by update operation list and broadcast to other editors.
     *
     * @param updateOperationDtoList operation dto list of self
     * @param ledgerIdToModelMap ledger id to model map
     * @param fundIdToModelMap fund id to model map
     * @param userId user id
     * @return transaction dto list
     */
    public List<TransactionDto> getTransactionsByUpdateOperationDtoListAndBroadcastToEditors(List<OperationDto> updateOperationDtoList,
                                                                                             Map<Long, LedgerModel> ledgerIdToModelMap,
                                                                                             Map<Long, FundModel> fundIdToModelMap,
                                                                                             Long userId
    ) throws SQLException {
        try(SqlSession session = sqlSessionFactory.openSession(false)) {
            try {
                List<TransactionDto> transactionDtoList = new ArrayList<>();
                for (OperationDto updateOperationDto : updateOperationDtoList) {
                    TransactionDto selectedUpdateTransactionDto = selectTransactionDtoByRowKeyAndTableName(updateOperationDto.getTargetRowKey(), updateOperationDto.getTargetTable());
                    transactionDtoList.add(selectedUpdateTransactionDto);
                    List<Long> transactionEditorIds = ledgerIdToModelMap.get(selectedUpdateTransactionDto.getLedgerId()).getLedgerInfo().getEditors();
                    List<Long> fundEditorIds = fundIdToModelMap.get(selectedUpdateTransactionDto.getFundId()).getFundInfo().getEditors();
                    if (transactionEditorIds.size() > 1 || fundEditorIds.size() > 1) {
                        broadcastOperationToEditors(userId, updateOperationDto, transactionEditorIds);
                        broadcastOperationToEditors(userId, updateOperationDto, fundEditorIds);
                    }
                }
                session.commit();
                return transactionDtoList;
            } catch (Exception e) {
                log.error("getTransactionsByUpdateOperationDtoListAndBroadcastToEditors sync error", e);
                session.rollback();
                throw new SQLException(e);
            }
        }
    }

    /**
     * Insert operation to editors.
     *
     * @param userId                    user id
     * @param selfUpdateOperationDto    update operation dto
     * @param editorIds                 editor ids
     */
    private void broadcastOperationToEditors(Long userId, OperationDto selfUpdateOperationDto, List<Long> editorIds){
        for (Long editorId : editorIds) {
            if (editorId.equals(userId)) {
                continue;
            }
            kafkaService.sendNewOperationMessage(
                    editorId,
                    selfUpdateOperationDto.getTargetTable(),
                    selfUpdateOperationDto.getTargetRowKey(),
                    selfUpdateOperationDto.getUpdateAt()
            );
        }
    }

    /**
     * Insert new transactions.
     *
     * @param insertTransactionDtoList insert transaction dto list {@link UpsertTransactionDto}
     * @return the number of rows affected
     * @throws SQLException sql exception
     */
    public int insertNewTransaction (List<UpsertTransactionDto> insertTransactionDtoList, Map<Long, FundModel> fundIdToModelMap) throws SQLException {
        int affectedRows = 0;
        try(SqlSession session = sqlSessionFactory.openSession(false)) {
            try {
                for (UpsertTransactionDto upsertTransactionDto : insertTransactionDtoList) {
                    affectedRows += transactionMapper.insertTransaction(upsertTransactionDto);
                    FundModel fundModel = fundIdToModelMap.get(upsertTransactionDto.getFundId());
                    if (Math.abs(upsertTransactionDto.getMoney()) < 1e-6f) {
                        continue;
                    }
                    if (TransactionTypeEnum.isPositive(upsertTransactionDto.getTypeId())) {
                        fundModel.getFundInfo().setBalance(fundModel.getFundInfo().getBalance() + upsertTransactionDto.getMoney());
                    } else {
                        fundModel.getFundInfo().setBalance(fundModel.getFundInfo().getBalance() - upsertTransactionDto.getMoney());
                    }
                    fundModel.getUpdateInfo().setUpdateAt(upsertTransactionDto.getUpdateAt());
                }
                session.commit();
            } catch (Exception e) {
                session.rollback();
                log.error("insertNewTransaction error", e);
                throw new SQLException(e);
            }
        }
        return affectedRows;
    }

    /**
     * Update transaction by row key.
     *
     * @param updateTransactionDtoList update transaction dto list {@link UpsertTransactionDto}
     * @return the number of rows affected
     * @throws SQLException sql exception
     */
    public int updateTransactionsByRowKey(List<UpsertTransactionDto> updateTransactionDtoList, Map<Long, FundModel> fundIdToModelMap) throws SQLException {
        int affectedRows = 0;
        try {
            for (UpsertTransactionDto upsertTransactionDto : updateTransactionDtoList) {
                if (Math.abs(upsertTransactionDto.getMoney()) > 1e-6f) {
                    TransactionModel oldTransactionModel = transactionMapper.getTransactionByRowKeyAndTableName(
                            upsertTransactionDto.getTableName(),
                            upsertTransactionDto.getRegionId(),
                            upsertTransactionDto.getLedgerId(),
                            upsertTransactionDto.getUserId(),
                            Long.MAX_VALUE - upsertTransactionDto.getTransactionId(),
                            null
                    );
                    FundModel fundModel = fundIdToModelMap.get(upsertTransactionDto.getFundId());
                    boolean isOldTransactionPositive = TransactionTypeEnum.isPositive(oldTransactionModel.getTransactionInfo().getTypeId());
                    boolean isNewTransactionPositive = TransactionTypeEnum.isPositive(upsertTransactionDto.getTypeId());
                    fundModel.getFundInfo().setBalance(fundModel.getFundInfo().getBalance()
                            + (isOldTransactionPositive ? -oldTransactionModel.getTransactionInfo().getMoney() : oldTransactionModel.getTransactionInfo().getMoney())
                            + (isNewTransactionPositive ? upsertTransactionDto.getMoney() : -upsertTransactionDto.getMoney()));
                    fundModel.getUpdateInfo().setUpdateAt(upsertTransactionDto.getUpdateAt());
                }
                affectedRows += transactionMapper.updateTransaction(upsertTransactionDto);
            }
        } catch (Exception e) {
            log.error("updateTransactionByRowKey error", e);
            throw new SQLException(e);
        }
        return affectedRows;
    }

    /**
     * Select transaction dto by row key.
     *
     * @param rowKey row key
     * @return transaction dto
     * @throws SQLException sql exception
     */
    public TransactionDto selectTransactionDtoByRowKeyAndTableName(String rowKey, String tableName) throws SQLException {
        String[] rowKeyParams = rowKey.split(com.pokemoney.hadoop.hbase.Constants.ROW_KEY_DELIMITER);
        Long reverseTransactionId = Long.parseLong(rowKeyParams[3]);
        TransactionModel transactionModel = transactionMapper.getTransactionByRowKeyAndTableName(tableName, Integer.parseInt(rowKeyParams[0]), Long.parseLong(rowKeyParams[1]), Long.parseLong(rowKeyParams[2]), reverseTransactionId, null);
        return getTransactionDtoFromTransactionModel(transactionModel);
    }

    /**
     * Get transaction.
     *
     * @param transactionId transaction id
     * @param selectedFieldsName the selected fields name
     * @return transaction dto
     * @throws SQLException sql exception
     */
    public TransactionDto getTransaction(UserModel userModel, Long transactionId, Long ledgerId, List<String> selectedFieldsName) throws SQLException {
        Long ledgerOwnerId = null;
        Integer regionId = null;
        for (String ledgerRowKeyInUserModel : userModel.getLedgerInfo().getLedgers()) {
            String[] ledgerRowKeyInUserModelSplit = ledgerRowKeyInUserModel.split(com.pokemoney.hadoop.hbase.Constants.ROW_KEY_DELIMITER);
            if (ledgerRowKeyInUserModelSplit[2].equals(ledgerId.toString())) {
                regionId = Integer.parseInt(ledgerRowKeyInUserModelSplit[0]);
                ledgerOwnerId = Long.parseLong(ledgerRowKeyInUserModelSplit[1]);
                break;
            }
        }
        if (ledgerOwnerId == null) {
            throw new SQLException("ledgerId not found in userModel");
        }
        String tableName = TransactionUtils.GetTableNameFromSnowflakeId(transactionId);
        Long reverseTransactionId = Long.MAX_VALUE - transactionId;
        TransactionModel transactionModel = transactionMapper.getTransactionByRowKeyAndTableName(tableName, regionId, ledgerId, ledgerOwnerId, reverseTransactionId, selectedFieldsName);
        return getTransactionDtoFromTransactionModel(transactionModel);
    }

    /**
     * Get transactions by filter.
     *
     * @param transactionFilter transaction filter
     * @param userId user id
     * @param ledgerId ledger id
     * @return transaction dto list
     * @throws GenericGraphQlForbiddenException generic graph ql forbidden exception
     */
    public List<TransactionDto> getTransactionsByFilter(TransactionFilter transactionFilter, Long userId, Long ledgerId) throws GenericGraphQlForbiddenException {
        throw new GenericGraphQlForbiddenException("not yet support for user now");
    }

    /**
     * Get transaction dto from transaction model.
     *
     * @param transactionModel transaction model
     * @return transaction dto
     * @throws SQLException sql exception
     */
    private TransactionDto getTransactionDtoFromTransactionModel(TransactionModel transactionModel) throws SQLException {
        return new TransactionDto(
                transactionModel.getTransactionInfo().getId(),
                transactionModel.getTransactionInfo().getMoney(),
                transactionModel.getTransactionInfo().getTypeId(),
                transactionModel.getTransactionInfo().getRelevantEntity(),
                transactionModel.getTransactionInfo().getComment(),
                transactionModel.getTransactionInfo().getFundId(),
                transactionModel.getTransactionInfo().getCategoryId(),
                transactionModel.getTransactionInfo().getSubcategoryId(),
                transactionModel.getTransactionInfo().getLedgerId(),
                transactionModel.getTransactionInfo().getHappenAt(),
                transactionModel.getUpdateBy(),
                transactionModel.getUpdateInfo().getUpdateAt(),
                transactionModel.getUpdateInfo().getDelFlag()
        );
    }
}
