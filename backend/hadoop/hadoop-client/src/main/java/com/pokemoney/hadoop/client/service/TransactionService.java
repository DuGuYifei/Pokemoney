package com.pokemoney.hadoop.client.service;

import com.pokemoney.hadoop.client.Constants;
import com.pokemoney.hadoop.client.vo.PreprocessedSyncTransactions;
import com.pokemoney.hadoop.hbase.dto.editor.EditorDto;
import com.pokemoney.hadoop.hbase.dto.filter.TransactionFilter;
import com.pokemoney.hadoop.hbase.dto.transaction.TransactionDto;
import com.pokemoney.hadoop.hbase.dto.transaction.UpsertTransactionDto;
import com.pokemoney.hadoop.hbase.dto.operation.OperationDto;
import com.pokemoney.hadoop.hbase.dto.sync.SyncTransactionInputDto;
import com.pokemoney.hadoop.hbase.dto.user.UserDto;
import com.pokemoney.hadoop.hbase.phoenix.dao.TransactionMapper;
import com.pokemoney.hadoop.hbase.phoenix.dao.OperationMapper;
import com.pokemoney.hadoop.hbase.phoenix.model.TransactionModel;
import com.pokemoney.hadoop.hbase.phoenix.model.OperationModel;
import com.pokemoney.hadoop.hbase.utils.RowKeyUtils;
import com.pokemoney.leaf.service.api.LeafGetRequestDto;
import com.pokemoney.leaf.service.api.LeafTriService;
import com.pokemoney.user.service.api.GetUserInfoRequestDto;
import com.pokemoney.user.service.api.GetUserInfoResponseDto;
import com.pokemoney.user.service.api.UserTriService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Transaction service
 */
@Slf4j
@Service
public class TransactionService {
    /**
     * The transaction mapper.
     */
    private final TransactionMapper transactionMapper;

    /**
     * The operation mapper.
     */
    private final OperationMapper operationMapper;

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
     * user triple api
     */
    @DubboReference(version = "1.0.0", protocol = "tri", group = "user", timeout = 10000)
    private final UserTriService userTriService;

    /**
     * Instantiates a new Transaction  service.
     *
     * @param transactionMapper      the transaction mapper
     * @param operationMapper   the operation mapper
     * @param sqlSessionFactory the sql session factory
     * @param dtpSyncExecutor1  the dynamic thread pool executor 1
     * @param leafTriService    leaf api
     * @param userTriService    user api
     */
    public TransactionService(TransactionMapper transactionMapper, OperationMapper operationMapper, SqlSessionFactory sqlSessionFactory, ThreadPoolExecutor dtpSyncExecutor1, LeafTriService leafTriService, UserTriService userTriService) {
        this.transactionMapper = transactionMapper;
        this.operationMapper = operationMapper;
        this.sqlSessionFactory = sqlSessionFactory;
        this.dtpSyncExecutor1 = dtpSyncExecutor1;
        this.leafTriService = leafTriService;
        this.userTriService = userTriService;
    }

    /**
     * Preprocess sync transaction.
     *
     * @param userId the user id
     * @param syncTransactionInputDtoList sync transaction input dto list {@link SyncTransactionInputDto}
     * @return the preprocessed sync transactions {@link PreprocessedSyncTransactions}
     */
    public PreprocessedSyncTransactions preprocessSyncTransaction(long syncOperationId, Long userId, String email, String username, SyncTransactionInputDto[] syncTransactionInputDtoList, LinkedList<OperationModel> operationModelTargetTransactionList, UserDto userDto) {
        PreprocessedSyncTransactions preprocessedSyncTransactions = new PreprocessedSyncTransactions(syncOperationId);
        List<UpsertTransactionDto> updateTransactionDtoList = preprocessedSyncTransactions.getUpdateTransactionDtoList();
        List<UpsertTransactionDto> insertTransactionDtoList = preprocessedSyncTransactions.getInsertTransactionDtoList();
        List<TransactionDto> returnTransactionDtoList = preprocessedSyncTransactions.getReturnTransactionDtoList();
        List<OperationDto> updateTransactionOperationDtoList = preprocessedSyncTransactions.getUpdateTransactionOperationDtoList();
        List<OperationDto> insertTransactionOperationDtoList = preprocessedSyncTransactions.getInsertTransactionOperationDtoList();
        List<SyncTransactionInputDto> noPermissionUpdateTransactionInputDtoList = preprocessedSyncTransactions.getNoPermissionUpdateTransactionInputDtoList();
        Long operationId = preprocessedSyncTransactions.getCurOperationId();
        // if transaction in the sync already exist in un-sync option, and update date is bigger than the un-sync one, then use new one
        for (SyncTransactionInputDto syncTransactionInputDto : syncTransactionInputDtoList) {
            boolean isExist = false;
            Long ownerId = syncTransactionInputDto.getOwner();
            if (ownerId.longValue() != userId.longValue()) {
                noPermissionUpdateTransactionInputDtoList.add(syncTransactionInputDto);
                continue;
            }
            Iterator<OperationModel> operationModelIterator = operationModelTargetTransactionList.iterator();
            while (operationModelIterator.hasNext()) {
                OperationModel operationModel = operationModelIterator.next();
                if (operationModel.getOperationInfo().getId().equals(syncTransactionInputDto.getTransactionId())) {
                    isExist = true;
                    if (operationModel.getUpdateAt() < syncTransactionInputDto.getUpdateAt()) {
                        operationModelIterator.remove();
                        Integer regionId = RowKeyUtils.getRegionId(ownerId);
                        Long transactionId = syncTransactionInputDto.getTransactionId();
                        preprocessSyncUpdateSituation(userDto, updateTransactionDtoList, syncTransactionInputDto, ownerId, transactionId, regionId);
                        operationId = Long.parseLong(leafTriService.getSnowflakeId(LeafGetRequestDto.newBuilder().setKey(Constants.LEAF_HBASE_OPERATION).build()).getId());
                        updateTransactionOperationDtoList.add(new OperationDto(
                                RowKeyUtils.getRegionId(ownerId),
                                ownerId,
                                Long.MAX_VALUE - operationId,
                                Long.toString(operationId),
                                com.pokemoney.hadoop.hbase.Constants.LEDGER_BOOK_TABLE,
                                RowKeyUtils.getTransactionRowKey(regionId.toString(), ownerId.toString(), transactionId.toString()),
                                syncTransactionInputDto.getUpdateAt()
                        ));
                    }
                    break;
                }
            }
            if (!isExist) {
                Long transactionId = syncTransactionInputDto.getTransactionId();
                operationId = Long.parseLong(leafTriService.getSnowflakeId(LeafGetRequestDto.newBuilder().setKey(Constants.LEAF_HBASE_OPERATION).build()).getId());
                if (transactionId < com.pokemoney.hadoop.hbase.Constants.MIN_SNOWFLAKE_ID) {
                    userDto.getTransactionInfo().getTransactionIds().add(transactionId);
                    transactionId = Long.parseLong(leafTriService.getSnowflakeId(LeafGetRequestDto.newBuilder().setKey(Constants.LEAF_HBASE_LEDGER_BOOK).build()).getId());
                    Integer regionId = RowKeyUtils.getRegionId(ownerId);
                    insertTransactionDtoList.add(new UpsertTransactionDto(
                            regionId,
                            ownerId,
                            transactionId,
                            syncTransactionInputDto.getName(),
                            ownerId,
                            new ArrayList<>() {{
                                add(ownerId);
                            }},
                            syncTransactionInputDto.getBalance(),
                            syncTransactionInputDto.getCreateAt(),
                            syncTransactionInputDto.getUpdateAt(),
                            syncTransactionInputDto.getDelFlag()
                    ));
                    insertTransactionOperationDtoList.add(new OperationDto(
                            regionId,
                            ownerId,
                            Long.MAX_VALUE - operationId,
                            Long.toString(operationId),
                            com.pokemoney.hadoop.hbase.Constants.LEDGER_BOOK_TABLE,
                            RowKeyUtils.getTransactionRowKey(regionId.toString(), ownerId.toString(), transactionId.toString()),
                            syncTransactionInputDto.getUpdateAt()
                    ));
                    returnTransactionDtoList.add(new TransactionDto(
                            transactionId,
                            syncTransactionInputDto.getName(),
                            syncTransactionInputDto.getBalance(),
                            ownerId,
                            new ArrayList<>() {{
                                add(new EditorDto(
                                        userId,
                                        email,
                                        username
                                ));
                            }},
                            syncTransactionInputDto.getCreateAt(),
                            syncTransactionInputDto.getUpdateAt(),
                            syncTransactionInputDto.getDelFlag()
                    ));
                } else {
                    Integer regionId = RowKeyUtils.getRegionId(ownerId);
                    preprocessSyncUpdateSituation(userDto, updateTransactionDtoList, syncTransactionInputDto, ownerId, transactionId, regionId);
                    updateTransactionOperationDtoList.add(new OperationDto(
                            regionId,
                            ownerId,
                            Long.MAX_VALUE - operationId,
                            Long.toString(operationId),
                            com.pokemoney.hadoop.hbase.Constants.LEDGER_BOOK_TABLE,
                            RowKeyUtils.getTransactionRowKey(regionId.toString(), ownerId.toString(), transactionId.toString()),
                            syncTransactionInputDto.getUpdateAt()
                    ));
                }
            }
        }
        return preprocessedSyncTransactions;
    }

    /**
     * Preprocess sync update situation.
     *
     * @param userDto user dto
     * @param updateTransactionDtoList update transaction dto list
     * @param syncTransactionInputDto sync transaction input dto
     * @param ownerId owner id
     * @param transactionId transaction id
     * @param regionId region id
     */
    private void preprocessSyncUpdateSituation(UserDto userDto, List<UpsertTransactionDto> updateTransactionDtoList, SyncTransactionInputDto syncTransactionInputDto, Long ownerId, Long transactionId, Integer regionId) {
        if (userDto.getTransactionInfo().getTransactionIds().contains(transactionId)) {
            if (syncTransactionInputDto.getDelFlag() == 1) {
                userDto.getTransactionInfo().getTransactionIds().remove(transactionId);
                userDto.getTransactionInfo().getDelTransactionIds().add(transactionId);
            }
        } else {
            if (syncTransactionInputDto.getDelFlag() == 0) {
                userDto.getTransactionInfo().getTransactionIds().add(transactionId);
                userDto.getTransactionInfo().getDelTransactionIds().remove(transactionId);
            }
        }
        updateTransactionDtoList.add(UpsertTransactionDto.builder()
                .regionId(regionId)
                .userId(ownerId)
                .transactionId(transactionId)
                .name(syncTransactionInputDto.getName())
                .balance(syncTransactionInputDto.getBalance())
                .owner(null)
                .editors(null)
                .createAt(null)
                .updateAt(syncTransactionInputDto.getUpdateAt())
                .delFlag(syncTransactionInputDto.getDelFlag())
                .build());
    }

    public List<TransactionDto> syncTransaction (PreprocessedSyncTransactions preprocessedSyncTransactions, LinkedList<OperationModel> operationModelTargetTransactionList) throws SQLException {
        Future<Integer> insertFuture = dtpSyncExecutor1.submit(() -> {
            try {
                return insertNewTransaction(preprocessedSyncTransactions.getInsertTransactionDtoList());
            } catch (SQLException e) {
                log.error("insertNewTransaction error", e);
                throw e;
            }
        });
        Future<List<TransactionDto>> updateFuture = dtpSyncExecutor1.submit(() -> {
            try(SqlSession session = sqlSessionFactory.openSession(false)) {
                try {
                    updateTransactionByRowKey(preprocessedSyncTransactions.getUpdateTransactionDtoList());
                    session.commit();
                    Future<List<TransactionDto>> transactionDtoFromUpdateOperationDtoFuture = dtpSyncExecutor1.submit(() -> getTransactionsByUpdateOperationDtoListAndBroadcastToEditors(preprocessedSyncTransactions.getUpdateTransactionOperationDtoList()));
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
            return updateFuture.get();
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
                transactionDtoList.add(selectTransactionDtoByRowKey(operationModel.getOperationInfo().getTargetRowKey()));
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
     * @param updateOperationDtoList operation model list
     * @return transaction dto list
     */
    public List<TransactionDto> getTransactionsByUpdateOperationDtoListAndBroadcastToEditors(List<OperationDto> updateOperationDtoList) throws SQLException {
        try(SqlSession session = sqlSessionFactory.openSession(false)) {
            try {
                List<TransactionDto> transactionDtoList = new ArrayList<>();
                for (OperationDto updateOperationDto : updateOperationDtoList) {
                    TransactionDto selectedUpdateTransactionDto = selectTransactionDtoByRowKey(updateOperationDto.getTargetRowKey());
                    transactionDtoList.add(selectedUpdateTransactionDto);
                    if (selectedUpdateTransactionDto.getEditors().size() > 1) {
                        for (EditorDto editorDto : selectedUpdateTransactionDto.getEditors()) {
                            if (editorDto.getUserId().equals(selectedUpdateTransactionDto.getOwner())) {
                                continue;
                            }
                            long broadcastOperationId = Long.parseLong(leafTriService.getSnowflakeId(LeafGetRequestDto.newBuilder().setKey(Constants.LEAF_HBASE_OPERATION).build()).getId());
                            OperationDto operationDto = new OperationDto(
                                    RowKeyUtils.getRegionId(editorDto.getUserId()),
                                    editorDto.getUserId(),
                                    Long.MAX_VALUE - broadcastOperationId,
                                    Long.toString(broadcastOperationId),
                                    com.pokemoney.hadoop.hbase.Constants.LEDGER_BOOK_TABLE,
                                    updateOperationDto.getTargetRowKey(),
                                    updateOperationDto.getUpdateAt()
                            );
                            operationMapper.insertOperation(operationDto);
                        }
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
     * Insert new transactions.
     *
     * @param insertTransactionDtoList insert transaction dto list {@link UpsertTransactionDto}
     * @return the number of rows affected
     * @throws SQLException sql exception
     */
    public int insertNewTransaction (List<UpsertTransactionDto> insertTransactionDtoList) throws SQLException {
        int affectedRows = 0;
        try(SqlSession session = sqlSessionFactory.openSession(false)) {
            try {
                for (UpsertTransactionDto upsertTransactionDto : insertTransactionDtoList) {
                    affectedRows += transactionMapper.insertTransaction(upsertTransactionDto);
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
    public int updateTransactionByRowKey (List<UpsertTransactionDto> updateTransactionDtoList) throws SQLException {
        int affectedRows = 0;
        try {
            for (UpsertTransactionDto upsertTransactionDto : updateTransactionDtoList) {
                affectedRows += transactionMapper.updateTransactionByRowKey(upsertTransactionDto);
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
    public TransactionDto selectTransactionDtoByRowKey (String rowKey) throws SQLException {
        String[] rowKeyParams = rowKey.split(com.pokemoney.hadoop.hbase.Constants.ROW_KEY_DELIMITER);
        TransactionModel transactionModel = transactionMapper.getTransactionByRowKey(Integer.parseInt(rowKeyParams[0]), Long.parseLong(rowKeyParams[1]), Long.parseLong(rowKeyParams[2]), null);
        return getTransactionDtoFromTransactionModel(transactionModel);
    }

    /**
     * Get transaction.
     *
     * @param transactionId transaction id
     * @param userId user id
     * @param selectedFieldsName the selected fields name
     * @return transaction dto
     * @throws SQLException sql exception
     */
    public TransactionDto getTransaction(Long transactionId, Long userId, List<String> selectedFieldsName) throws SQLException {
        Integer regionId = RowKeyUtils.getRegionId(userId);
        TransactionModel transactionModel = transactionMapper.getTransactionByRowKey(regionId, userId, transactionId, selectedFieldsName);
        return getTransactionDtoFromTransactionModel(transactionModel);
    }

    /**
     * Get transactions.
     *
     * @param userId user id
     * @param transactionFilter transaction filter
     * @param selectedFieldsName the selected fields name
     * @return transaction dto list
     */
    public List<TransactionDto> getTransactions(Long userId, TransactionFilter transactionFilter, List<String> selectedFieldsName) throws SQLException {
        Integer regionId = RowKeyUtils.getRegionId(userId);
        List<TransactionModel> transactionModelList = transactionMapper.getTransactionsByUserAndFilter(regionId, userId, transactionFilter, selectedFieldsName);
        List<TransactionDto> transactionDtoList = new ArrayList<>();
        for (TransactionModel transactionModel : transactionModelList) {
            transactionDtoList.add(getTransactionDtoFromTransactionModel(transactionModel));
        }
        return transactionDtoList;
    }

    /**
     * Get transaction dto from transaction model.
     *
     * @param transactionModel transaction model
     * @return transaction dto
     * @throws SQLException sql exception
     */
    private TransactionDto getTransactionDtoFromTransactionModel(TransactionModel transactionModel) throws SQLException {
        List<EditorDto> editors = new ArrayList<>();
        java.sql.Array editorsArray = transactionModel.getTransactionInfo().getEditors();
        Long[] editorIds = (Long[]) editorsArray.getArray();
        for (Long editorId : editorIds) {
            GetUserInfoResponseDto getUserInfoResponseDto = userTriService.getUserInfo(GetUserInfoRequestDto.newBuilder().setUserId(editorId).build());
            editors.add(new EditorDto(
                    editorId,
                    getUserInfoResponseDto.getEmail(),
                    getUserInfoResponseDto.getUsername()
            ));
        }
        editorsArray.free();
        return new TransactionDto(
                transactionModel.getTransactionId(),
                transactionModel.getTransactionInfo().getName(),
                transactionModel.getTransactionInfo().getBalance(),
                transactionModel.getTransactionInfo().getOwner(),
                editors,
                transactionModel.getTransactionInfo().getCreateAt(),
                transactionModel.getUpdateInfo().getUpdateAt(),
                transactionModel.getUpdateInfo().getDelFlag()
        );
    }
}
