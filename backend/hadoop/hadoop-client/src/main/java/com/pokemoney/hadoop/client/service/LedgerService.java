package com.pokemoney.hadoop.client.service;

import com.pokemoney.hadoop.client.kafka.KafkaService;
import com.pokemoney.hadoop.client.vo.PreprocessedSyncLedgers;
import com.pokemoney.hadoop.client.vo.ProcessedSyncLedgers;
import com.pokemoney.hadoop.hbase.dto.editor.EditorDto;
import com.pokemoney.hadoop.hbase.dto.filter.LedgerFilter;
import com.pokemoney.hadoop.hbase.dto.ledger.LedgerDto;
import com.pokemoney.hadoop.hbase.dto.ledger.UpsertLedgerDto;
import com.pokemoney.hadoop.hbase.dto.operation.OperationDto;
import com.pokemoney.hadoop.hbase.dto.sync.SyncLedgerInputDto;
import com.pokemoney.hadoop.hbase.dto.user.UserDto;
import com.pokemoney.hadoop.hbase.phoenix.dao.LedgerMapper;
import com.pokemoney.hadoop.hbase.phoenix.dao.OperationMapper;
import com.pokemoney.hadoop.hbase.phoenix.model.LedgerModel;
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
import java.util.*;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * Ledger book service
 */
@Slf4j
@Service
public class LedgerService {
    /**
     * The Kafka service.
     */
    private final KafkaService kafkaService;
    /**
     * The ledger mapper.
     */
    private final LedgerMapper ledgerMapper;

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
     * Instantiates a new Ledger book service.
     *
     * @param kafkaService      the kafka service
     * @param ledgerMapper      the ledger mapper
     * @param operationMapper   the operation mapper
     * @param sqlSessionFactory the sql session factory
     * @param dtpSyncExecutor1  the dynamic thread pool executor 1
     * @param leafTriService    leaf api
     * @param userTriService    user api
     */
    public LedgerService(KafkaService kafkaService, LedgerMapper ledgerMapper, OperationMapper operationMapper, SqlSessionFactory sqlSessionFactory, ThreadPoolExecutor dtpSyncExecutor1, LeafTriService leafTriService, UserTriService userTriService) {
        this.kafkaService = kafkaService;
        this.ledgerMapper = ledgerMapper;
        this.operationMapper = operationMapper;
        this.sqlSessionFactory = sqlSessionFactory;
        this.dtpSyncExecutor1 = dtpSyncExecutor1;
        this.leafTriService = leafTriService;
        this.userTriService = userTriService;
    }

    /**
     * Preprocess sync ledger.
     *
     * @param syncOperationId sync operation id
     * @param syncLedgerInputDtoList sync ledger input dto list {@link SyncLedgerInputDto}
     * @param operationModelTargetLedgerList operation model target ledger list {@link OperationModel}
     * @param userDto user dto {@link UserDto}
     * @return the preprocessed sync ledgers {@link PreprocessedSyncLedgers}
     */
    public PreprocessedSyncLedgers preprocessSyncLedger(Long syncOperationId, List<SyncLedgerInputDto> syncLedgerInputDtoList, LinkedList<OperationModel> operationModelTargetLedgerList, UserDto userDto) {
        PreprocessedSyncLedgers preprocessedSyncLedgers = new PreprocessedSyncLedgers(syncOperationId);
        List<UpsertLedgerDto> updateLedgerDtoList = preprocessedSyncLedgers.getUpdateLedgerDtoList();
        List<UpsertLedgerDto> insertLedgerDtoList = preprocessedSyncLedgers.getInsertLedgerDtoList();
        List<OperationDto> ledgerOperationDtoList = preprocessedSyncLedgers.getLedgerOperationDtoList();
        List<SyncLedgerInputDto> noPermissionUpdateLedgerInputDtoList = preprocessedSyncLedgers.getNoPermissionUpdateLedgerInputDtoList();
        Map<Long, Long> newLedgerIdAndSnowflakeIdMap = preprocessedSyncLedgers.getNewLedgerIdAndSnowflakeIdMap();
        Long operationId = preprocessedSyncLedgers.getCurOperationId();
        // if ledger in the sync already exist in un-sync option, and update date is bigger than the un-sync one, then use new one
        System.out.println("syncLedgerInputDtoList: " + syncLedgerInputDtoList);
        for (SyncLedgerInputDto syncLedgerInputDto : syncLedgerInputDtoList) {
            boolean isExist = false;
            Long ownerId = syncLedgerInputDto.getOwner();
            if (ownerId.longValue() != userDto.getUserId().longValue()) {
                noPermissionUpdateLedgerInputDtoList.add(syncLedgerInputDto);
                continue;
            }
            Iterator<OperationModel> operationModelIterator = operationModelTargetLedgerList.iterator();
            while (operationModelIterator.hasNext()) {
                OperationModel operationModel = operationModelIterator.next();
                if (operationModel.getOperationInfo().getOperationId().equals(syncLedgerInputDto.getLedgerId())) {
                    isExist = true;
                    if (operationModel.getUpdateAt() < syncLedgerInputDto.getUpdateAt()) {
                        operationModelIterator.remove();
                        Integer regionId = RowKeyUtils.getRegionId(ownerId);
                        Long ledgerId = syncLedgerInputDto.getLedgerId();
                        String ledgerRowKey = RowKeyUtils.getLedgerRowKey(regionId.toString(), ownerId.toString(), ledgerId.toString());
                        preprocessSyncUpdateSituation(userDto, updateLedgerDtoList, syncLedgerInputDto, ownerId, ledgerRowKey, ledgerId, regionId);
                        operationId = Long.parseLong(leafTriService.getSnowflakeId(LeafGetRequestDto.newBuilder().setKey(com.pokemoney.hadoop.hbase.Constants.LEAF_HBASE_OPERATION).build()).getId());
                        ledgerOperationDtoList.add(new OperationDto(
                                RowKeyUtils.getRegionId(ownerId),
                                ownerId,
                                Long.MAX_VALUE - operationId,
                                operationId,
                                com.pokemoney.hadoop.hbase.Constants.LEDGER_BOOK_TABLE,
                                RowKeyUtils.getLedgerRowKey(regionId.toString(), ownerId.toString(), ledgerId.toString()),
                                syncLedgerInputDto.getUpdateAt()
                        ));
                    }
                    break;
                }
            }
            if (!isExist) {
                Long ledgerId = syncLedgerInputDto.getLedgerId();
                Integer regionId = RowKeyUtils.getRegionId(ownerId);
                operationId = Long.parseLong(leafTriService.getSnowflakeId(LeafGetRequestDto.newBuilder().setKey(com.pokemoney.hadoop.hbase.Constants.LEAF_HBASE_OPERATION).build()).getId());
                if (ledgerId < com.pokemoney.hadoop.hbase.Constants.MIN_SNOWFLAKE_ID) {
                    Long oldLedgerId = ledgerId;
                    ledgerId = Long.parseLong(leafTriService.getSnowflakeId(LeafGetRequestDto.newBuilder().setKey(com.pokemoney.hadoop.hbase.Constants.LEAF_HBASE_LEDGER_BOOK).build()).getId());
                    newLedgerIdAndSnowflakeIdMap.put(oldLedgerId, ledgerId);
                    String ledgerRowKey = RowKeyUtils.getLedgerRowKey(regionId.toString(), ownerId.toString(), ledgerId.toString());
                    userDto.getLedgerInfo().getLedgers().add(ledgerRowKey);
                    insertLedgerDtoList.add(new UpsertLedgerDto(
                            regionId,
                            ownerId,
                            ledgerId,
                            syncLedgerInputDto.getName(),
                            ownerId,
                            List.of(ownerId),
                            syncLedgerInputDto.getBudget(),
                            syncLedgerInputDto.getCreateAt(),
                            syncLedgerInputDto.getUpdateAt(),
                            syncLedgerInputDto.getDelFlag()
                    ));
                    ledgerOperationDtoList.add(new OperationDto(
                            regionId,
                            ownerId,
                            Long.MAX_VALUE - operationId,
                            operationId,
                            com.pokemoney.hadoop.hbase.Constants.LEDGER_BOOK_TABLE,
                            RowKeyUtils.getLedgerRowKey(regionId.toString(), ownerId.toString(), ledgerId.toString()),
                            syncLedgerInputDto.getUpdateAt()
                    ));
                } else {
                    String ledgerRowKey = RowKeyUtils.getLedgerRowKey(regionId.toString(), ownerId.toString(), ledgerId.toString());
                    preprocessSyncUpdateSituation(userDto, updateLedgerDtoList, syncLedgerInputDto, ownerId, ledgerRowKey, ledgerId, regionId);
                    ledgerOperationDtoList.add(new OperationDto(
                            regionId,
                            ownerId,
                            Long.MAX_VALUE - operationId,
                            operationId,
                            com.pokemoney.hadoop.hbase.Constants.LEDGER_BOOK_TABLE,
                            RowKeyUtils.getLedgerRowKey(regionId.toString(), ownerId.toString(), ledgerId.toString()),
                            syncLedgerInputDto.getUpdateAt()
                    ));
                }
            }
        }
        preprocessedSyncLedgers.setCurOperationId(operationId);
        return preprocessedSyncLedgers;
    }

    /**
     * Preprocess sync update situation.
     *
     * @param userDto user dto
     * @param updateLedgerDtoList update ledger dto list
     * @param syncLedgerInputDto sync ledger input dto
     * @param ownerId owner id
     * @param ledgerId ledger id
     * @param regionId region id
     */
    private void preprocessSyncUpdateSituation(UserDto userDto, List<UpsertLedgerDto> updateLedgerDtoList, SyncLedgerInputDto syncLedgerInputDto, Long ownerId, String ledgerRowKey, Long ledgerId, Integer regionId) {
        if (userDto.getLedgerInfo().getLedgers().contains(ledgerRowKey)) {
            if (syncLedgerInputDto.getDelFlag() == 1) {
                userDto.getLedgerInfo().getLedgers().remove(ledgerRowKey);
                userDto.getLedgerInfo().getDelLedgers().add(ledgerRowKey);
            }
        } else {
            if (syncLedgerInputDto.getDelFlag() == 0) {
                userDto.getLedgerInfo().getLedgers().add(ledgerRowKey);
                userDto.getLedgerInfo().getDelLedgers().remove(ledgerRowKey);
            }
        }
        updateLedgerDtoList.add(UpsertLedgerDto.builder()
                .regionId(regionId)
                .userId(ownerId)
                .ledgerId(ledgerId)
                .name(syncLedgerInputDto.getName())
                .budget(syncLedgerInputDto.getBudget())
                .owner(null)
                .editors(null)
                .createAt(null)
                .updateAt(syncLedgerInputDto.getUpdateAt())
                .delFlag(syncLedgerInputDto.getDelFlag())
                .build());
    }

    /**
     * Sync ledger.
     *
     * @param preprocessedSyncLedgers preprocessed sync ledgers
     * @param operationModelTargetLedgerList operation model target ledger list {@link OperationModel}
     * @return processed sync ledgers {@link ProcessedSyncLedgers}
     * @throws SQLException sql exception
     */
    public ProcessedSyncLedgers syncLedger (PreprocessedSyncLedgers preprocessedSyncLedgers, LinkedList<OperationModel> operationModelTargetLedgerList) throws SQLException {
         Future<Integer> insertFuture = dtpSyncExecutor1.submit(() -> {
             try {
                 return insertNewLedger(preprocessedSyncLedgers.getInsertLedgerDtoList());
             } catch (SQLException e) {
                 log.error("insertNewLedger error", e);
                 throw e;
             }
         });
        Future<List<LedgerDto>> updateFuture = dtpSyncExecutor1.submit(() -> {
            try(SqlSession session = sqlSessionFactory.openSession(false)) {
                try {
                    updateLedgersByRowKey(preprocessedSyncLedgers.getUpdateLedgerDtoList());
                    session.commit();
                    try {
                        insertFuture.get();
                    } catch (Exception e) {
                        log.error("insertNewLedger error", e);
                        throw new SQLException(e);
                    }
                    Future<List<LedgerDto>> ledgerDtoFromOperationDtoFuture = dtpSyncExecutor1.submit(() -> getLedgersByOperationDtoListAndBroadcastToEditors(preprocessedSyncLedgers.getLedgerOperationDtoList()));
                    preprocessedSyncLedgers.getReturnLedgerDtoList().addAll(getLedgersByOperationModelList(operationModelTargetLedgerList));
                    try {
                        preprocessedSyncLedgers.getReturnLedgerDtoList().addAll(ledgerDtoFromOperationDtoFuture.get());
                    } catch (Exception e) {
                        log.error("extract from getLedgersByUpdateOperationDtoListAndBroadcastToEditors error", e);
                        throw new SQLException(e);
                    }
                    return preprocessedSyncLedgers.getReturnLedgerDtoList();
                } catch (SQLException e) {
                    log.error("updateLedgerByRowKey or broadcast error", e);
                    session.rollback();
                    throw new RuntimeException(e);
                }
            }
        });
        try {
            return new ProcessedSyncLedgers(
                    updateFuture.get(),
                    preprocessedSyncLedgers.getNoPermissionUpdateLedgerInputDtoList(),
                    preprocessedSyncLedgers.getCurOperationId(),
                    preprocessedSyncLedgers.getNewLedgerIdAndSnowflakeIdMap()
            );
        } catch (Exception e) {
            log.error("syncLedger error", e);
            throw new SQLException(e);
        }
    }

    /**
     * Get ledgers by operation list.
     *
     * @param operationModelList operation model list
     * @return ledger dto list
     * @throws SQLException sql exception
     */
    public List<LedgerDto> getLedgersByOperationModelList(List<OperationModel> operationModelList) throws SQLException {
        List<LedgerDto> ledgerDtoList = new ArrayList<>();
        try {
            for (OperationModel operationModel : operationModelList) {
                ledgerDtoList.add(selectLedgerDtoByRowKey(operationModel.getOperationInfo().getTargetRowKey()));
            }
        } catch (Exception e) {
            log.error("getLedgersByOperationList error", e);
            throw new SQLException(e);
        }
        return ledgerDtoList;
    }

    /**
     * Get ledgers by update operation list and broadcast to other editors.
     *
     * @param operationDtoList operation model list
1     * @return ledger dto list
     */
    public List<LedgerDto> getLedgersByOperationDtoListAndBroadcastToEditors(List<OperationDto> operationDtoList) throws SQLException {
        try(SqlSession session = sqlSessionFactory.openSession(false)) {
            try {
                List<LedgerDto> ledgerDtoList = new ArrayList<>();
                for (OperationDto updateOperationDto : operationDtoList) {
                    LedgerDto selectedUpdateLedgerDto = selectLedgerDtoByRowKey(updateOperationDto.getTargetRowKey());
                    ledgerDtoList.add(selectedUpdateLedgerDto);
                    operationMapper.insertOperation(updateOperationDto);
                    if (selectedUpdateLedgerDto.getEditors().size() > 1) {
                        for (EditorDto editorDto : selectedUpdateLedgerDto.getEditors()) {
                            if (editorDto.getUserId().equals(selectedUpdateLedgerDto.getOwner())) {
                                continue;
                            }
                            kafkaService.sendNewOperationMessage(
                                    editorDto.getUserId(),
                                    com.pokemoney.hadoop.hbase.Constants.LEDGER_BOOK_TABLE,
                                    updateOperationDto.getTargetRowKey(),
                                    updateOperationDto.getUpdateAt()
                            );
                        }
                    }
                }
                session.commit();
                return ledgerDtoList;
            } catch (Exception e) {
                log.error("getLedgersByUpdateOperationDtoListAndBroadcastToEditors sync error", e);
                session.rollback();
                throw new SQLException(e);
            }
        }
    }

    /**
     * Insert new ledgers.
     *
     * @param insertLedgerDtoList insert ledger dto list {@link UpsertLedgerDto}
     * @return the number of rows affected
     * @throws SQLException sql exception
     */
    public int insertNewLedger (List<UpsertLedgerDto> insertLedgerDtoList) throws SQLException {
        int affectedRows = 0;
        try(SqlSession session = sqlSessionFactory.openSession(false)) {
            try {
                for (UpsertLedgerDto upsertLedgerDto : insertLedgerDtoList) {
                    affectedRows += ledgerMapper.insertLedger(upsertLedgerDto);
                }
                session.commit();
            } catch (Exception e) {
                session.rollback();
                log.error("insertNewLedger error", e);
                throw new SQLException(e);
            }
        }
        return affectedRows;
    }

    /**
     * Update ledger by row key.
     *
     * @param updateLedgerDtoList update ledger dto list {@link UpsertLedgerDto}
     * @return the number of rows affected
     * @throws SQLException sql exception
     */
    public int updateLedgersByRowKey(List<UpsertLedgerDto> updateLedgerDtoList) throws SQLException {
        int affectedRows = 0;
        try {
            for (UpsertLedgerDto upsertLedgerDto : updateLedgerDtoList) {
                affectedRows += ledgerMapper.updateLedgerByRowKey(upsertLedgerDto);
            }
        } catch (Exception e) {
            log.error("updateLedgerByRowKey error", e);
            throw new SQLException(e);
        }
        return affectedRows;
    }

    /**
     * Select ledger dto by row key.
     *
     * @param rowKey row key
     * @return ledger dto
     * @throws SQLException sql exception
     */
    public LedgerDto selectLedgerDtoByRowKey (String rowKey) throws SQLException {
        String[] rowKeyParams = rowKey.split(com.pokemoney.hadoop.hbase.Constants.ROW_KEY_DELIMITER);
        LedgerModel ledgerModel = ledgerMapper.getLedgerByRowKey(Integer.parseInt(rowKeyParams[0]), Long.parseLong(rowKeyParams[1]), Long.parseLong(rowKeyParams[2]), null);
        return getLedgerDtoFromLedgerModel(ledgerModel);
    }

    /**
     * Get ledger.
     *
     * @param ledgerId ledger id
     * @param userId user id
     * @param selectedFieldsName the selected fields name
     * @return ledger dto
     * @throws SQLException sql exception
     */
    public LedgerDto getLedger(Long ledgerId, Long userId, List<String> selectedFieldsName) throws SQLException {
        Integer regionId = RowKeyUtils.getRegionId(userId);
        LedgerModel ledgerModel = ledgerMapper.getLedgerByRowKey(regionId, userId, ledgerId, selectedFieldsName);
        return getLedgerDtoFromLedgerModel(ledgerModel);
    }

    /**
     * Get ledger model
     *
     * @param ledgerId ledger id
     * @param userId user id
     * @param selectedFieldsName the selected fields name
     * @return ledger model
     * @throws SQLException sql exception
     */
    public LedgerModel getLedgerModel(Long ledgerId, Long userId, List<String> selectedFieldsName) throws SQLException {
        Integer regionId = RowKeyUtils.getRegionId(userId);
        return ledgerMapper.getLedgerByRowKey(regionId, userId, ledgerId, selectedFieldsName);
    }

    /**
     * Get ledgers by filter.
     *
     * @param userId user id
     * @param ledgerFilter ledger filter
     * @param selectedFieldsName the selected fields name
     * @return ledger dto list
     */
    public List<LedgerDto> getLedgersByFilter(Long userId, LedgerFilter ledgerFilter, List<String> selectedFieldsName) throws SQLException {
        Integer regionId = RowKeyUtils.getRegionId(userId);
        List<LedgerModel> ledgerModelList = ledgerMapper.getLedgersByUserAndFilter(regionId, userId, ledgerFilter, selectedFieldsName);
        List<LedgerDto> ledgerDtoList = new ArrayList<>();
        for (LedgerModel ledgerModel : ledgerModelList) {
            ledgerDtoList.add(getLedgerDtoFromLedgerModel(ledgerModel));
        }
        return ledgerDtoList;
    }

    /**
     * Get ledger dto from ledger model.
     *
     * @param ledgerModel ledger model
     * @return ledger dto
     */
    private LedgerDto getLedgerDtoFromLedgerModel(LedgerModel ledgerModel){
        List<Long> editorIds = ledgerModel.getLedgerInfo().getEditors();
        List<EditorDto> editors = new ArrayList<>();
        for (Long editorId : editorIds) {
            GetUserInfoResponseDto getUserInfoResponseDto = userTriService.getUserInfo(GetUserInfoRequestDto.newBuilder().setUserId(editorId).build());
            editors.add(new EditorDto(
                    editorId,
                    getUserInfoResponseDto.getEmail(),
                    getUserInfoResponseDto.getUsername()
            ));
        }
        return new LedgerDto(
                ledgerModel.getLedgerId(),
                ledgerModel.getLedgerInfo().getName(),
                ledgerModel.getLedgerInfo().getBudget(),
                ledgerModel.getLedgerInfo().getOwner(),
                editors,
                ledgerModel.getLedgerInfo().getCreateAt(),
                ledgerModel.getUpdateInfo().getUpdateAt(),
                ledgerModel.getUpdateInfo().getDelFlag()
        );
    }
}
