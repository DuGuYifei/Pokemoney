package com.pokemoney.hadoop.client.service;

import com.pokemoney.hadoop.client.Constants;
import com.pokemoney.hadoop.client.vo.PreprocessedSyncFunds;
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
     * @param fundMapper      the fund mapper
     * @param operationMapper   the operation mapper
     * @param sqlSessionFactory the sql session factory
     * @param dtpSyncExecutor1  the dynamic thread pool executor 1
     * @param leafTriService    leaf api
     * @param userTriService    user api
     */
    public FundService(FundMapper fundMapper, OperationMapper operationMapper, SqlSessionFactory sqlSessionFactory, ThreadPoolExecutor dtpSyncExecutor1, LeafTriService leafTriService, UserTriService userTriService) {
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
     * @param syncFundInputDtoList sync fund input dto list {@link SyncFundInputDto}
     * @param operationModelTargetFundList operation model target fund list {@link OperationModel}
     * @param userDto user dto {@link UserDto}
     * @return the preprocessed sync funds {@link PreprocessedSyncFunds}
     */
    public PreprocessedSyncFunds preprocessSyncFund(Long syncOperationId, SyncFundInputDto[] syncFundInputDtoList, LinkedList<OperationModel> operationModelTargetFundList, UserDto userDto) {
        PreprocessedSyncFunds preprocessedSyncFunds = new PreprocessedSyncFunds(syncOperationId);
        List<UpsertFundDto> updateFundDtoList = preprocessedSyncFunds.getUpdateFundDtoList();
        List<UpsertFundDto> insertFundDtoList = preprocessedSyncFunds.getInsertFundDtoList();
        List<FundDto> returnFundDtoList = preprocessedSyncFunds.getReturnFundDtoList();
        List<OperationDto> updateFundOperationDtoList = preprocessedSyncFunds.getUpdateFundOperationDtoList();
        List<OperationDto> insertFundOperationDtoList = preprocessedSyncFunds.getInsertFundOperationDtoList();
        List<SyncFundInputDto> noPermissionUpdateFundInputDtoList = preprocessedSyncFunds.getNoPermissionUpdateFundInputDtoList();
        Long operationId = preprocessedSyncFunds.getCurOperationId();
        // if fund in the sync already exist in un-sync option, and update date is bigger than the un-sync one, then use new one
        for (SyncFundInputDto syncFundInputDto : syncFundInputDtoList) {
            boolean isExist = false;
            Long ownerId = syncFundInputDto.getOwner();
            if (ownerId.longValue() != userDto.getUserId().longValue()) {
                noPermissionUpdateFundInputDtoList.add(syncFundInputDto);
                continue;
            }
            Iterator<OperationModel> operationModelIterator = operationModelTargetFundList.iterator();
            while (operationModelIterator.hasNext()) {
                OperationModel operationModel = operationModelIterator.next();
                if (operationModel.getOperationInfo().getId().equals(syncFundInputDto.getFundId())) {
                    isExist = true;
                    if (operationModel.getUpdateAt() < syncFundInputDto.getUpdateAt()) {
                        operationModelIterator.remove();
                        Integer regionId = RowKeyUtils.getRegionId(ownerId);
                        Long fundId = syncFundInputDto.getFundId();
                        preprocessSyncUpdateSituation(userDto, updateFundDtoList, syncFundInputDto, ownerId, fundId, regionId);
                        operationId = Long.parseLong(leafTriService.getSnowflakeId(LeafGetRequestDto.newBuilder().setKey(Constants.LEAF_HBASE_OPERATION).build()).getId());
                        updateFundOperationDtoList.add(new OperationDto(
                                RowKeyUtils.getRegionId(ownerId),
                                ownerId,
                                Long.MAX_VALUE - operationId,
                                Long.toString(operationId),
                                com.pokemoney.hadoop.hbase.Constants.FUND_TABLE,
                                RowKeyUtils.getFundRowKey(regionId.toString(), ownerId.toString(), fundId.toString()),
                                syncFundInputDto.getUpdateAt()
                        ));
                    }
                    break;
                }
            }
            if (!isExist) {
                Long fundId = syncFundInputDto.getFundId();
                operationId = Long.parseLong(leafTriService.getSnowflakeId(LeafGetRequestDto.newBuilder().setKey(Constants.LEAF_HBASE_OPERATION).build()).getId());
                if (fundId < com.pokemoney.hadoop.hbase.Constants.MIN_SNOWFLAKE_ID) {
                    userDto.getFundInfo().getFundIds().add(fundId);
                    fundId = Long.parseLong(leafTriService.getSnowflakeId(LeafGetRequestDto.newBuilder().setKey(Constants.LEAF_HBASE_FUND).build()).getId());
                    Integer regionId = RowKeyUtils.getRegionId(ownerId);
                    insertFundDtoList.add(new UpsertFundDto(
                            regionId,
                            ownerId,
                            fundId,
                            syncFundInputDto.getName(),
                            ownerId,
                            new ArrayList<>() {{
                                add(ownerId);
                            }},
                            syncFundInputDto.getBalance(),
                            syncFundInputDto.getCreateAt(),
                            syncFundInputDto.getUpdateAt(),
                            syncFundInputDto.getDelFlag()
                    ));
                    insertFundOperationDtoList.add(new OperationDto(
                            regionId,
                            ownerId,
                            Long.MAX_VALUE - operationId,
                            Long.toString(operationId),
                            com.pokemoney.hadoop.hbase.Constants.FUND_TABLE,
                            RowKeyUtils.getFundRowKey(regionId.toString(), ownerId.toString(), fundId.toString()),
                            syncFundInputDto.getUpdateAt()
                    ));
                    returnFundDtoList.add(new FundDto(
                            fundId,
                            syncFundInputDto.getName(),
                            syncFundInputDto.getBalance(),
                            ownerId,
                            new ArrayList<>() {{
                                add(new EditorDto(
                                        userDto.getUserId(),
                                        userDto.getEmail(),
                                        userDto.getName()
                                ));
                            }},
                            syncFundInputDto.getCreateAt(),
                            syncFundInputDto.getUpdateAt(),
                            syncFundInputDto.getDelFlag()
                    ));
                } else {
                    Integer regionId = RowKeyUtils.getRegionId(ownerId);
                    preprocessSyncUpdateSituation(userDto, updateFundDtoList, syncFundInputDto, ownerId, fundId, regionId);
                    updateFundOperationDtoList.add(new OperationDto(
                            regionId,
                            ownerId,
                            Long.MAX_VALUE - operationId,
                            Long.toString(operationId),
                            com.pokemoney.hadoop.hbase.Constants.FUND_TABLE,
                            RowKeyUtils.getFundRowKey(regionId.toString(), ownerId.toString(), fundId.toString()),
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
    private void preprocessSyncUpdateSituation(UserDto userDto, List<UpsertFundDto> updateFundDtoList, SyncFundInputDto syncFundInputDto, Long ownerId, Long fundId, Integer regionId) {
        if (userDto.getFundInfo().getFundIds().contains(fundId)) {
            if (syncFundInputDto.getDelFlag() == 1) {
                userDto.getFundInfo().getFundIds().remove(fundId);
                userDto.getFundInfo().getDelFundIds().add(fundId);
            }
        } else {
            if (syncFundInputDto.getDelFlag() == 0) {
                userDto.getFundInfo().getFundIds().add(fundId);
                userDto.getFundInfo().getDelFundIds().remove(fundId);
            }
        }
        updateFundDtoList.add(UpsertFundDto.builder()
                .regionId(regionId)
                .userId(ownerId)
                .fundId(fundId)
                .name(syncFundInputDto.getName())
                .balance(syncFundInputDto.getBalance())
                .owner(null)
                .editors(null)
                .createAt(null)
                .updateAt(syncFundInputDto.getUpdateAt())
                .delFlag(syncFundInputDto.getDelFlag())
                .build());
    }

    public PreprocessedSyncFunds syncFund (PreprocessedSyncFunds preprocessedSyncFunds, LinkedList<OperationModel> operationModelTargetFundList) throws SQLException {
        Future<Integer> insertFuture = dtpSyncExecutor1.submit(() -> {
            try {
                return insertNewFund(preprocessedSyncFunds.getInsertFundDtoList());
            } catch (SQLException e) {
                log.error("insertNewFund error", e);
                throw e;
            }
        });
        Future<PreprocessedSyncFunds> updateFuture = dtpSyncExecutor1.submit(() -> {
            try(SqlSession session = sqlSessionFactory.openSession(false)) {
                try {
                    updateFundByRowKey(preprocessedSyncFunds.getUpdateFundDtoList());
                    session.commit();
                    Future<List<FundDto>> fundDtoFromUpdateOperationDtoFuture = dtpSyncExecutor1.submit(() -> getFundsByUpdateOperationDtoListAndBroadcastToEditors(preprocessedSyncFunds.getUpdateFundOperationDtoList()));
                    preprocessedSyncFunds.getReturnFundDtoList().addAll(getFundsByOperationModelList(operationModelTargetFundList));
                    try {
                        preprocessedSyncFunds.getReturnFundDtoList().addAll(fundDtoFromUpdateOperationDtoFuture.get());
                    } catch (Exception e) {
                        log.error("extract from getFundsByUpdateOperationDtoListAndBroadcastToEditors error", e);
                        throw new SQLException(e);
                    }
                    return preprocessedSyncFunds;
                } catch (SQLException e) {
                    log.error("updateFundByRowKey or broadcast error", e);
                    session.rollback();
                    throw new RuntimeException(e);
                }
            }
        });
        try {
            insertFuture.get();
            return updateFuture.get();
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
     * @param updateOperationDtoList operation model list
     * @return fund dto list
     */
    public List<FundDto> getFundsByUpdateOperationDtoListAndBroadcastToEditors(List<OperationDto> updateOperationDtoList) throws SQLException {
        try(SqlSession session = sqlSessionFactory.openSession(false)) {
            try {
                List<FundDto> fundDtoList = new ArrayList<>();
                for (OperationDto updateOperationDto : updateOperationDtoList) {
                    FundDto selectedUpdateFundDto = selectFundDtoByRowKey(updateOperationDto.getTargetRowKey());
                    fundDtoList.add(selectedUpdateFundDto);
                    if (selectedUpdateFundDto.getEditors().size() > 1) {
                        for (EditorDto editorDto : selectedUpdateFundDto.getEditors()) {
                            if (editorDto.getUserId().equals(selectedUpdateFundDto.getOwner())) {
                                continue;
                            }
                            long broadcastOperationId = Long.parseLong(leafTriService.getSnowflakeId(LeafGetRequestDto.newBuilder().setKey(Constants.LEAF_HBASE_OPERATION).build()).getId());
                            OperationDto operationDto = new OperationDto(
                                    RowKeyUtils.getRegionId(editorDto.getUserId()),
                                    editorDto.getUserId(),
                                    Long.MAX_VALUE - broadcastOperationId,
                                    Long.toString(broadcastOperationId),
                                    com.pokemoney.hadoop.hbase.Constants.FUND_TABLE,
                                    updateOperationDto.getTargetRowKey(),
                                    updateOperationDto.getUpdateAt()
                            );
                            operationMapper.insertOperation(operationDto);
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
    public int updateFundByRowKey (List<UpsertFundDto> updateFundDtoList) throws SQLException {
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
     * @throws SQLException sql exception
     */
    public FundDto selectFundDtoByRowKey (String rowKey) throws SQLException {
        String[] rowKeyParams = rowKey.split(com.pokemoney.hadoop.hbase.Constants.ROW_KEY_DELIMITER);
        FundModel fundModel = fundMapper.getFundByRowKey(Integer.parseInt(rowKeyParams[0]), Long.parseLong(rowKeyParams[1]), Long.parseLong(rowKeyParams[2]), null);
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
        Integer regionId = RowKeyUtils.getRegionId(userId);
        FundModel fundModel = fundMapper.getFundByRowKey(regionId, userId, fundId, selectedFieldsName);
        return getFundDtoFromFundModel(fundModel);
    }

    /**
     * Get funds.
     *
     * @param userId user id
     * @param fundFilter fund filter
     * @param selectedFieldsName the selected fields name
     * @return fund dto list
     */
    public List<FundDto> getFunds(Long userId, FundFilter fundFilter, List<String> selectedFieldsName) throws SQLException {
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
     * @throws SQLException sql exception
     */
    private FundDto getFundDtoFromFundModel(FundModel fundModel) throws SQLException {
        List<EditorDto> editors = new ArrayList<>();
        java.sql.Array editorsArray = fundModel.getFundInfo().getEditors();
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
        return new FundDto(
                fundModel.getFundId(),
                fundModel.getFundInfo().getName(),
                fundModel.getFundInfo().getBalance(),
                fundModel.getFundInfo().getOwner(),
                editors,
                fundModel.getFundInfo().getCreateAt(),
                fundModel.getUpdateInfo().getUpdateAt(),
                fundModel.getUpdateInfo().getDelFlag()
        );
    }
}
