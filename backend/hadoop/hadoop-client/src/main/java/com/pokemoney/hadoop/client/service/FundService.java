package com.pokemoney.hadoop.client.service;

import com.pokemoney.hadoop.client.kafka.KafkaService;
import com.pokemoney.hadoop.client.vo.PreprocessedSyncFunds;
import com.pokemoney.hadoop.client.vo.ProcessedSyncFunds;
import com.pokemoney.hadoop.hbase.dto.editor.EditorDto;
import com.pokemoney.hadoop.hbase.dto.filter.FundFilter;
import com.pokemoney.hadoop.hbase.dto.fund.FundDto;
import com.pokemoney.hadoop.hbase.dto.fund.UpsertFundDto;
import com.pokemoney.hadoop.hbase.dto.operation.OperationDto;
import com.pokemoney.hadoop.hbase.dto.sync.SyncFundInputDto;
import com.pokemoney.hadoop.hbase.dto.user.UserDto;
import com.pokemoney.hadoop.hbase.phoenix.dao.FundMapper;
import com.pokemoney.hadoop.hbase.phoenix.dao.OperationMapper;
import com.pokemoney.hadoop.hbase.phoenix.model.FundModel;
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
 * Fund  service
 */
@Slf4j
@Service
public class FundService {
    /**
     * The Kafka service.
     */
    private final KafkaService kafkaService;
    /**
     * The fund mapper.
     */
    private final FundMapper fundMapper;

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
     * Instantiates a new Fund  service.
     *
     * @param kafkaService      the kafka service
     * @param fundMapper        the fund mapper
     * @param operationMapper   the operation mapper
     * @param sqlSessionFactory the sql session factory
     * @param dtpSyncExecutor1  the dynamic thread pool executor 1
     * @param leafTriService    leaf api
     * @param userTriService    user api
     */
    public FundService(KafkaService kafkaService, FundMapper fundMapper, OperationMapper operationMapper, SqlSessionFactory sqlSessionFactory, ThreadPoolExecutor dtpSyncExecutor1, LeafTriService leafTriService, UserTriService userTriService) {
        this.kafkaService = kafkaService;
        this.fundMapper = fundMapper;
        this.operationMapper = operationMapper;
        this.sqlSessionFactory = sqlSessionFactory;
        this.dtpSyncExecutor1 = dtpSyncExecutor1;
        this.leafTriService = leafTriService;
        this.userTriService = userTriService;
    }

    /**
     * Preprocess sync fund.
     *
     * @param syncOperationId sync operation id
     * @param syncFundInputDtoList sync fund input dto list {@link SyncFundInputDto}
     * @param operationModelTargetFundList operation model target fund list {@link OperationModel}
     * @param userDto user dto {@link UserDto}
     * @return the preprocessed sync funds {@link PreprocessedSyncFunds}
     */
    public PreprocessedSyncFunds preprocessSyncFund(Long syncOperationId, List<SyncFundInputDto> syncFundInputDtoList, LinkedList<OperationModel> operationModelTargetFundList, UserDto userDto) {
        PreprocessedSyncFunds preprocessedSyncFunds = new PreprocessedSyncFunds(syncOperationId);
        List<UpsertFundDto> updateFundDtoList = preprocessedSyncFunds.getUpdateFundDtoList();
        List<UpsertFundDto> insertFundDtoList = preprocessedSyncFunds.getInsertFundDtoList();
        List<OperationDto> fundOperationDtoList = preprocessedSyncFunds.getFundOperationDtoList();
        List<SyncFundInputDto> noPermissionUpdateFundInputDtoList = preprocessedSyncFunds.getNoPermissionUpdateFundInputDtoList();
        Long operationId = preprocessedSyncFunds.getCurOperationId();
        Map<Long, Long> newFundIdAndSnowflakeIdMap = preprocessedSyncFunds.getNewFundIdAndSnowflakeIdMap();
        // if fund in the sync already exist in un-sync option, and update date is bigger than the un-sync one, then use new one
        System.out.println("syncFundInputDtoList: " + syncFundInputDtoList);
        for (SyncFundInputDto syncFundInputDto : syncFundInputDtoList) {
            boolean isExist = false;
            Long ownerId = syncFundInputDto.getOwner();
            if (ownerId.longValue() != userDto.getUserId().longValue()) {
                noPermissionUpdateFundInputDtoList.add(syncFundInputDto);
                continue;
            }
            Iterator<OperationModel> operationModelIterator = operationModelTargetFundList.iterator();
            System.out.println("operationModelTargetFundList: " + operationModelTargetFundList);
            while (operationModelIterator.hasNext()) {
                System.out.println("进入model循环");
                OperationModel operationModel = operationModelIterator.next();
                if (operationModel.getOperationInfo().getOperationId().equals(syncFundInputDto.getFundId())) {
                    isExist = true;
                    if (operationModel.getUpdateAt() < syncFundInputDto.getUpdateAt()) {
                        operationModelIterator.remove();
                        Integer regionId = RowKeyUtils.getRegionId(ownerId);
                        Long fundId = syncFundInputDto.getFundId();
                        String fundRowKey = RowKeyUtils.getFundRowKey(regionId.toString(), ownerId.toString(), fundId.toString());
                        preprocessSyncUpdateSituation(userDto, updateFundDtoList, syncFundInputDto, ownerId, fundRowKey, fundId, regionId);
                        operationId = Long.parseLong(leafTriService.getSnowflakeId(LeafGetRequestDto.newBuilder().setKey(com.pokemoney.hadoop.hbase.Constants.LEAF_HBASE_OPERATION).build()).getId());
                        fundOperationDtoList.add(new OperationDto(
                                RowKeyUtils.getRegionId(ownerId),
                                ownerId,
                                Long.MAX_VALUE - operationId,
                                operationId,
                                com.pokemoney.hadoop.hbase.Constants.FUND_TABLE,
                                fundRowKey,
                                syncFundInputDto.getUpdateAt()
                        ));
                    }
                    break;
                }
            }
            System.out.println("isExist: " + isExist);
            if (!isExist) {
                Long fundId = syncFundInputDto.getFundId();
                operationId = Long.parseLong(leafTriService.getSnowflakeId(LeafGetRequestDto.newBuilder().setKey(com.pokemoney.hadoop.hbase.Constants.LEAF_HBASE_OPERATION).build()).getId());
                Integer regionId = RowKeyUtils.getRegionId(ownerId);
                if (fundId < com.pokemoney.hadoop.hbase.Constants.MIN_SNOWFLAKE_ID) {
                    System.out.println("fundId: " + fundId);
                    Long oldFundId = fundId;
                    fundId = Long.parseLong(leafTriService.getSnowflakeId(LeafGetRequestDto.newBuilder().setKey(com.pokemoney.hadoop.hbase.Constants.LEAF_HBASE_FUND).build()).getId());
                    newFundIdAndSnowflakeIdMap.put(oldFundId, fundId);
                    String fundRowKey = RowKeyUtils.getFundRowKey(regionId.toString(), ownerId.toString(), fundId.toString());
                    System.out.println("fundRowKey: " + fundRowKey);
                    userDto.getFundInfo().getFunds().add(fundRowKey);
                    List<Long> editorIds = List.of(ownerId);
                    insertFundDtoList.add(new UpsertFundDto(
                            regionId,
                            ownerId,
                            fundId,
                            syncFundInputDto.getName(),
                            ownerId,
                            editorIds,
                            syncFundInputDto.getBalance(),
                            syncFundInputDto.getCreateAt(),
                            syncFundInputDto.getUpdateAt(),
                            syncFundInputDto.getDelFlag()
                    ));
                    System.out.println("insertFundDtoList: " + insertFundDtoList);
                    fundOperationDtoList.add(new OperationDto(
                            regionId,
                            ownerId,
                            Long.MAX_VALUE - operationId,
                            operationId,
                            com.pokemoney.hadoop.hbase.Constants.FUND_TABLE,
                            fundRowKey,
                            syncFundInputDto.getUpdateAt()
                    ));
                } else {
                    String fundRowKey = RowKeyUtils.getFundRowKey(regionId.toString(), ownerId.toString(), fundId.toString());
                    preprocessSyncUpdateSituation(userDto, updateFundDtoList, syncFundInputDto, ownerId, fundRowKey, fundId, regionId);
                    fundOperationDtoList.add(new OperationDto(
                            regionId,
                            ownerId,
                            Long.MAX_VALUE - operationId,
                            operationId,
                            com.pokemoney.hadoop.hbase.Constants.FUND_TABLE,
                            fundRowKey,
                            syncFundInputDto.getUpdateAt()
                    ));
                }
            }
        }
        preprocessedSyncFunds.setCurOperationId(operationId);
        return preprocessedSyncFunds;
    }

    /**
     * Preprocess sync update situation.
     *
     * @param userDto user dto
     * @param updateFundDtoList update fund dto list
     * @param syncFundInputDto sync fund input dto
     * @param ownerId owner id
     * @param fundId fund id
     * @param regionId region id
     */
    private void preprocessSyncUpdateSituation(UserDto userDto, List<UpsertFundDto> updateFundDtoList, SyncFundInputDto syncFundInputDto, Long ownerId, String fundRowKey, Long fundId, Integer regionId) {
        if (userDto.getFundInfo().getFunds().contains(fundRowKey)) {
            if (syncFundInputDto.getDelFlag() == 1) {
                userDto.getFundInfo().getFunds().remove(fundRowKey);
                userDto.getFundInfo().getDelFunds().add(fundRowKey);
            }
        } else {
            if (syncFundInputDto.getDelFlag() == 0) {
                userDto.getFundInfo().getFunds().add(fundRowKey);
                userDto.getFundInfo().getDelFunds().remove(fundRowKey);
            }
        }
        updateFundDtoList.add(UpsertFundDto.builder()
                .regionId(regionId)
                .userId(ownerId)
                .fundId(fundId)
                .name(syncFundInputDto.getName())
                .balance(null)
                .owner(null)
                .editors(null)
                .createAt(null)
                .updateAt(syncFundInputDto.getUpdateAt())
                .delFlag(syncFundInputDto.getDelFlag())
                .build());
    }

    /**
     * Sync fund.
     *
     * @param preprocessedSyncFunds preprocessed sync funds
     * @param operationModelTargetFundList operation model target fund list {@link OperationModel}
     * @return processed sync funds {@link ProcessedSyncFunds}
     * @throws SQLException sql exception
     */
    public ProcessedSyncFunds syncFund (PreprocessedSyncFunds preprocessedSyncFunds, LinkedList<OperationModel> operationModelTargetFundList) throws SQLException {
        Future<Integer> insertFuture = dtpSyncExecutor1.submit(() -> {
            try {
                System.out.println("insertNewFund: " + preprocessedSyncFunds.getInsertFundDtoList());
                return insertNewFund(preprocessedSyncFunds.getInsertFundDtoList());
            } catch (SQLException e) {
                log.error("insertNewFund error", e);
                throw e;
            }
        });
        Future<List<FundDto>> updateFuture = dtpSyncExecutor1.submit(() -> {
            try(SqlSession session = sqlSessionFactory.openSession(false)) {
                try {
                    updateFundsByRowKey(preprocessedSyncFunds.getUpdateFundDtoList());
                    session.commit();
                    try {
                        insertFuture.get();
                    } catch (Exception e) {
                        log.error("insertNewFund error", e);
                        throw new SQLException(e);
                    }
                    Future<List<FundDto>> fundDtoFromOperationDtoFuture = dtpSyncExecutor1.submit(() -> getFundsByFundOperationDtoListAndBroadcastToEditors(preprocessedSyncFunds.getFundOperationDtoList()));
                    preprocessedSyncFunds.getReturnFundDtoList().addAll(getFundsByOperationModelList(operationModelTargetFundList));
                    try {
                        preprocessedSyncFunds.getReturnFundDtoList().addAll(fundDtoFromOperationDtoFuture.get());
                    } catch (Exception e) {
                        log.error("extract from getFundsByUpdateOperationDtoListAndBroadcastToEditors error", e);
                        throw new SQLException(e);
                    }
                    return preprocessedSyncFunds.getReturnFundDtoList();
                } catch (SQLException e) {
                    log.error("updateFundByRowKey or broadcast error", e);
                    session.rollback();
                    throw new RuntimeException(e);
                }
            }
        });
        try {
            return new ProcessedSyncFunds(
                    updateFuture.get(),
                    preprocessedSyncFunds.getNoPermissionUpdateFundInputDtoList(),
                    preprocessedSyncFunds.getCurOperationId(),
                    preprocessedSyncFunds.getNewFundIdAndSnowflakeIdMap()
            );
        } catch (Exception e) {
            log.error("syncFund error", e);
            throw new SQLException(e);
        }
    }

    /**
     * Get funds by operation list.
     *
     * @param operationModelList operation model list
     * @return fund dto list
     * @throws SQLException sql exception
     */
    public List<FundDto> getFundsByOperationModelList(List<OperationModel> operationModelList) throws SQLException {
        List<FundDto> fundDtoList = new ArrayList<>();
        try {
            for (OperationModel operationModel : operationModelList) {
                fundDtoList.add(selectFundDtoByRowKey(operationModel.getOperationInfo().getTargetRowKey()));
            }
        } catch (Exception e) {
            log.error("getFundsByOperationList error", e);
            throw new SQLException(e);
        }
        return fundDtoList;
    }

    /**
     * Get funds by update operation list and broadcast to other editors.
     *
     * @param fundOperationDtoList operation model list
     * @return fund dto list
     */
    public List<FundDto> getFundsByFundOperationDtoListAndBroadcastToEditors(List<OperationDto> fundOperationDtoList) throws SQLException {
        try(SqlSession session = sqlSessionFactory.openSession(false)) {
            try {
                List<FundDto> fundDtoList = new ArrayList<>();
                for (OperationDto updateOperationDto : fundOperationDtoList) {
                    FundDto selectedUpdateFundDto = selectFundDtoByRowKey(updateOperationDto.getTargetRowKey());
                    fundDtoList.add(selectedUpdateFundDto);
                    operationMapper.insertOperation(updateOperationDto);
                    if (selectedUpdateFundDto.getEditors().size() > 1) {
                        for (EditorDto editorDto : selectedUpdateFundDto.getEditors()) {
                            if (editorDto.getUserId().equals(selectedUpdateFundDto.getOwner())) {
                                continue;
                            }
                            kafkaService.sendNewOperationMessage(
                                    editorDto.getUserId(),
                                    updateOperationDto.getTargetTable(),
                                    updateOperationDto.getTargetRowKey(),
                                    updateOperationDto.getUpdateAt()
                            );
                        }
                    }
                }
                session.commit();
                return fundDtoList;
            } catch (Exception e) {
                log.error("getFundsByUpdateOperationDtoListAndBroadcastToEditors sync error", e);
                session.rollback();
                throw new SQLException(e);
            }
        }
    }

    public Integer broadcastFundByFundModels(List<FundModel> fundModels, Long userId){
        int affectedRows = 0;
        Long operationId = Long.parseLong(leafTriService.getSnowflakeId(LeafGetRequestDto.newBuilder().setKey(com.pokemoney.hadoop.hbase.Constants.LEAF_HBASE_OPERATION).build()).getId());
        for (FundModel fundModel : fundModels) {
            UpsertFundDto upsertFundDto = UpsertFundDto.builder()
                    .regionId(fundModel.getRegionId())
                    .userId(fundModel.getFundInfo().getOwner())
                    .fundId(fundModel.getFundId())
                    .name(fundModel.getFundInfo().getName())
                    .balance(fundModel.getFundInfo().getBalance())
                    .updateAt(fundModel.getUpdateInfo().getUpdateAt())
                    .delFlag(fundModel.getUpdateInfo().getDelFlag())
                    .build();
            fundMapper.updateFundByRowKey(upsertFundDto);
            OperationDto operationDto = new OperationDto(
                    fundModel.getRegionId(),
                    userId,
                    Long.MAX_VALUE - operationId,
                    operationId,
                    com.pokemoney.hadoop.hbase.Constants.FUND_TABLE,
                    RowKeyUtils.getFundRowKey(fundModel.getRegionId().toString(), fundModel.getFundInfo().getOwner().toString(), fundModel.getFundId().toString()),
                    System.currentTimeMillis()
            );
            affectedRows += operationMapper.insertOperation(operationDto);
            for (Long editorId : fundModel.getFundInfo().getEditors()) {
                if (editorId.equals(userId)) {
                    continue;
                }
                kafkaService.sendNewOperationMessage(
                        editorId,
                        com.pokemoney.hadoop.hbase.Constants.FUND_TABLE,
                        RowKeyUtils.getFundRowKey(fundModel.getRegionId().toString(), fundModel.getFundInfo().getOwner().toString(), fundModel.getFundId().toString()),
                        System.currentTimeMillis()
                );
            }
        }
        return affectedRows;
    }

    /**
     * Insert new funds.
     *
     * @param insertFundDtoList insert fund dto list {@link UpsertFundDto}
     * @return the number of rows affected
     * @throws SQLException sql exception
     */
    public int insertNewFund (List<UpsertFundDto> insertFundDtoList) throws SQLException {
        int affectedRows = 0;
        try(SqlSession session = sqlSessionFactory.openSession(false)) {
            try {
                for (UpsertFundDto upsertFundDto : insertFundDtoList) {
                    affectedRows += fundMapper.insertFund(upsertFundDto);
                }
                session.commit();
            } catch (Exception e) {
                session.rollback();
                log.error("insertNewFund error", e);
                throw new SQLException(e);
            }
        }
        return affectedRows;
    }

    /**
     * Update fund by row key.
     *
     * @param updateFundDtoList update fund dto list {@link UpsertFundDto}
     * @return the number of rows affected
     * @throws SQLException sql exception
     */
    public int updateFundsByRowKey(List<UpsertFundDto> updateFundDtoList) throws SQLException {
        int affectedRows = 0;
        try {
            for (UpsertFundDto upsertFundDto : updateFundDtoList) {
                affectedRows += fundMapper.updateFundByRowKey(upsertFundDto);
            }
        } catch (Exception e) {
            log.error("updateFundByRowKey error", e);
            throw new SQLException(e);
        }
        return affectedRows;
    }

    /**
     * Select fund dto by row key.
     *
     * @param rowKey row key
     * @return fund dto
     */
    public FundDto selectFundDtoByRowKey (String rowKey){
        System.out.println("rowKey: " + rowKey);
        String[] rowKeyParams = rowKey.split(com.pokemoney.hadoop.hbase.Constants.ROW_KEY_DELIMITER);
        System.out.println("1");
        System.out.println("rowKeyParams: " + Arrays.toString(rowKeyParams));
        System.out.println("Long of rowKeyParams[0]: " + Long.parseLong(rowKeyParams[0]));
        System.out.println("Long of rowKeyParams[1]: " + Long.parseLong(rowKeyParams[1]));
        System.out.println("Long of rowKeyParams[2]: " + Long.parseLong(rowKeyParams[2]));
        FundModel fundModel = fundMapper.getFundByRowKey(Integer.parseInt(rowKeyParams[0]), Long.parseLong(rowKeyParams[1]), Long.parseLong(rowKeyParams[2]), null);
        System.out.println("fundModel: " + fundModel);
        return getFundDtoFromFundModel(fundModel);
    }

    /**
     * Get fund.
     *
     * @param fundId fund id
     * @param userId user id
     * @param selectedFieldsName the selected fields name
     * @return fund dto
     * @throws SQLException sql exception
     */
    public FundDto getFund(Long fundId, Long userId, List<String> selectedFieldsName) throws SQLException {
        FundModel fundModel = getFundModel(fundId, userId, selectedFieldsName);
        return getFundDtoFromFundModel(fundModel);
    }

    /**
     * Get fund model.
     *
     * @param fundId fund id
     * @param userId user id
     * @param selectedFieldsName the selected fields name
     * @return fund model
     * @throws SQLException sql exception
     */
    public FundModel getFundModel(Long fundId, Long userId, List<String> selectedFieldsName) throws SQLException {
        Integer regionId = RowKeyUtils.getRegionId(userId);
        return fundMapper.getFundByRowKey(regionId, userId, fundId, selectedFieldsName);
    }

    /**
     * Get funds by filter.
     *
     * @param userId user id
     * @param fundFilter fund filter
     * @param selectedFieldsName the selected fields name
     * @return fund dto list
     */
    public List<FundDto> getFundsByFilter(Long userId, FundFilter fundFilter, List<String> selectedFieldsName) throws SQLException {
        Integer regionId = RowKeyUtils.getRegionId(userId);
        List<FundModel> fundModelList = fundMapper.getFundsByUserAndFilter(regionId, userId, fundFilter, selectedFieldsName);
        List<FundDto> fundDtoList = new ArrayList<>();
        for (FundModel fundModel : fundModelList) {
            fundDtoList.add(getFundDtoFromFundModel(fundModel));
        }
        return fundDtoList;
    }

    /**
     * Get fund dto from fund model.
     *
     * @param fundModel fund model
     * @return fund dto
     */
    private FundDto getFundDtoFromFundModel(FundModel fundModel) {
        List<Long> editorIds = fundModel.getFundInfo().getEditors();
        List<EditorDto> editorDtoList = new ArrayList<>();
        for (Long editorId : editorIds) {
            GetUserInfoResponseDto getUserInfoResponseDto = userTriService.getUserInfo(GetUserInfoRequestDto.newBuilder().setUserId(editorId).build());
            editorDtoList.add(new EditorDto(
                    editorId,
                    getUserInfoResponseDto.getEmail(),
                    getUserInfoResponseDto.getUsername()
            ));
        }
        return new FundDto(
                fundModel.getFundId(),
                fundModel.getFundInfo().getName(),
                fundModel.getFundInfo().getBalance(),
                fundModel.getFundInfo().getOwner(),
                editorDtoList,
                fundModel.getFundInfo().getCreateAt(),
                fundModel.getUpdateInfo().getUpdateAt(),
                fundModel.getUpdateInfo().getDelFlag()
        );
    }

    /**
     * Add fund model to fund dto list and broadcast.
     *
     * @param fundModels fund model
     * @param returnSyncFunds return sync funds
     * @return fund dto list
     */
    public List<FundDto> addFundModelListToFundDtoListDoUpdateAndBroadcast(List<FundModel> fundModels, List<FundDto> returnSyncFunds) {
        try (SqlSession session = sqlSessionFactory.openSession(false)) {
            try {
                for (FundModel fundModel : fundModels) {
                    returnSyncFunds.add(getFundDtoFromFundModel(fundModel));
                    kafkaService.sendNewOperationMessage(
                            fundModel.getFundInfo().getOwner(),
                            com.pokemoney.hadoop.hbase.Constants.FUND_TABLE,
                            RowKeyUtils.getFundRowKey(fundModel.getRegionId().toString(), fundModel.getFundInfo().getOwner().toString(), fundModel.getFundId().toString()),
                            fundModel.getUpdateInfo().getUpdateAt()
                    );
                    UpsertFundDto upsertFundDto = UpsertFundDto.builder()
                            .regionId(fundModel.getRegionId())
                            .userId(fundModel.getFundInfo().getOwner())
                            .fundId(fundModel.getFundId())
                            .name(fundModel.getFundInfo().getName())
                            .balance(fundModel.getFundInfo().getBalance())
                            .owner(fundModel.getFundInfo().getOwner())
                            .editors(fundModel.getFundInfo().getEditors())
                            .createAt(fundModel.getFundInfo().getCreateAt())
                            .updateAt(fundModel.getUpdateInfo().getUpdateAt())
                            .delFlag(fundModel.getUpdateInfo().getDelFlag())
                            .build();
                    fundMapper.updateFundByRowKey(upsertFundDto);
                }
                session.commit();
            } catch (Exception e) {
                log.error("addFundModelListToFundDtoListDoUpdateAndBroadcast error", e);
                session.rollback();
                throw new RuntimeException(e);
            }
        }
        return returnSyncFunds;
    }
}
