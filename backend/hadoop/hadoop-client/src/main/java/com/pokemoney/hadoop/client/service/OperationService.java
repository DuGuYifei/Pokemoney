package com.pokemoney.hadoop.client.service;

import com.pokemoney.hadoop.client.Constants;
import com.pokemoney.hadoop.client.vo.DividedOperationLists;
import com.pokemoney.hadoop.hbase.dto.operation.OperationDto;
import com.pokemoney.hadoop.hbase.phoenix.dao.OperationMapper;
import com.pokemoney.hadoop.hbase.phoenix.model.OperationModel;
import com.pokemoney.hadoop.hbase.utils.RowKeyUtils;
import com.pokemoney.leaf.service.api.LeafTriService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.List;

/**
 * Operation table service
 */
@Slf4j
@Service
public class OperationService {
    /**
     * Operation mapper
     */
    private final OperationMapper operationMapper;

    /**
     * The sql session factory.
     */
    private final SqlSessionFactory sqlSessionFactory;

    /**
     * leaf triple service
     */
    @DubboReference(version = "1.0.0", protocol = "tri", group = "leaf", timeout = 10000)
    private final LeafTriService leafTriService;

    /**
     * Constructor
     *
     * @param operationMapper   operation mapper
     * @param sqlSessionFactory sql session factory
     * @param leafTriService    leaf triple service
     */
    public OperationService(OperationMapper operationMapper, SqlSessionFactory sqlSessionFactory, LeafTriService leafTriService) {
        this.operationMapper = operationMapper;
        this.sqlSessionFactory = sqlSessionFactory;
        this.leafTriService = leafTriService;
    }

    /**
     * Insert operation
     *
     * @param operationsListArray array of operations list
     * @return affected rows
     * @throws SQLException sql exception
     */
    public Integer insertOperations(List<List<OperationDto>> operationsListArray) throws SQLException {
        int affectedRows = 0;
        // 如果每个元素都是空的，那么就不需要插入了
        boolean allEmpty = true;
        for (List<OperationDto> operationsList : operationsListArray) {
            if (operationsList != null && !operationsList.isEmpty()) {
                allEmpty = false;
                break;
            }
        }
        if (allEmpty) {
            return affectedRows;
        }
        try(SqlSession sqlSession = sqlSessionFactory.openSession(false)) {
            try {
                for (List<OperationDto> operationsList : operationsListArray) {
                    for (OperationDto operationDto : operationsList) {
                        affectedRows += operationMapper.insertOperation(operationDto);
                    }
                }
                sqlSession.commit();
            } catch (Exception e) {
                sqlSession.rollback();
                throw new SQLException(e);
            }
        }
        return affectedRows;
    }

    /**
     * Get operations with larger operation id than the given operation id by limit.
     *
     * @param userId user id
     * @param operationId operation id
     * @return operation dtos in linked list
     */
    public List<OperationModel> getOperationsByOperationId(long userId, long operationId) {
        int regionId = RowKeyUtils.getRegionId(userId);
        long reverseOperationId = Long.MAX_VALUE - operationId;
        List<OperationModel> result;
        if (operationId == 0) {
            result = operationMapper.getOperationsLowerReverseIdDistinctTargetRowKeyWithLimit(regionId, userId, reverseOperationId, Constants.TransactionLazyTransferNum);
        } else {
            result = operationMapper.getOperationsLowerReverseIdDistinctTargetRowKey(regionId, userId, reverseOperationId);
        }
        return result;
    }

    /**
     * Separate operations into different List by table name.
     *
     * @param operationsList operations list
     * @return divided operation lists
     */
    public DividedOperationLists divideOperationList(List<OperationModel> operationsList) {
        DividedOperationLists dividedOperationLists = new DividedOperationLists();
        dividedOperationLists.setMaxOperationId(0L);
        for (OperationModel operationModel : operationsList) {
            String tableName = operationModel.getOperationInfo().getTargetTable();
            switch (tableName.charAt(2)) {
                case 'l':
                    dividedOperationLists.getLedgerOperationList().add(operationModel);
                    dividedOperationLists.setMaxOperationId(Math.max(dividedOperationLists.getMaxOperationId(), operationModel.getOperationInfo().getOperationId()));
                    break;
                case 'f':
                    dividedOperationLists.getFundOperationList().add(operationModel);
                    dividedOperationLists.setMaxOperationId(Math.max(dividedOperationLists.getMaxOperationId(), operationModel.getOperationInfo().getOperationId()));
                    break;
                case 't':
                    dividedOperationLists.getTransactionOperationList().add(operationModel);
                    dividedOperationLists.setMaxOperationId(Math.max(dividedOperationLists.getMaxOperationId(), operationModel.getOperationInfo().getOperationId()));
                    break;
                default:
                    break;
            }
        }
        return dividedOperationLists;
    }
}
