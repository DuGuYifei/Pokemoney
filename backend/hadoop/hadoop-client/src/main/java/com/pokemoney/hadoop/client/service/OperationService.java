package com.pokemoney.hadoop.client.service;

import com.pokemoney.hadoop.client.Constants;
import com.pokemoney.hadoop.client.vo.DividedOperationLists;
import com.pokemoney.hadoop.hbase.dto.ledger.UpsertLedgerDto;
import com.pokemoney.hadoop.hbase.dto.operation.OperationDto;
import com.pokemoney.hadoop.hbase.phoenix.dao.OperationMapper;
import com.pokemoney.hadoop.hbase.phoenix.model.OperationModel;
import com.pokemoney.hadoop.hbase.utils.RowKeyUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

/**
 * Operation table service
 */
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
     * Constructor
     *
     * @param operationMapper   operation mapper
     * @param sqlSessionFactory sql session factory
     */
    public OperationService(OperationMapper operationMapper, SqlSessionFactory sqlSessionFactory) {
        this.operationMapper = operationMapper;
        this.sqlSessionFactory = sqlSessionFactory;
    }

    /**
     * Insert operation
     *
     * @param operationsListArray array of operations list
     * @return affected rows
     * @throws SQLException sql exception
     */
    public int insertOperations(List<OperationDto>[] operationsListArray) throws SQLException {
        int affectedRows = 0;
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
        if (operationId == 0) {
            return operationMapper.getOperationsLowerReverseIdDistinctTargetRowKeyWithLimit(regionId, userId, reverseOperationId, Constants.TransactionLazyTransferNum);
        }
        return operationMapper.getOperationsLowerReverseIdDistinctTargetRowKey(regionId, userId, reverseOperationId);
    }

    /**
     * Separate operations into different List by table name.
     *
     * @param operationsList operations list
     * @return divided operation lists
     */
    public DividedOperationLists divideOperationList(List<OperationModel> operationsList) {
        DividedOperationLists dividedOperationLists = new DividedOperationLists();
        for (OperationModel operationModel : operationsList) {
            String tableName = operationModel.getOperationInfo().getTargetTable();
            switch (tableName.charAt(2)) {
                case 'l':
                    dividedOperationLists.getLedgerOperationList().add(operationModel);
                    break;
                case 'f':
                    dividedOperationLists.getFundOperationList().add(operationModel);
                    break;
                case 't':
                    dividedOperationLists.getTransactionOperationList().add(operationModel);
                    break;
                default:
                    break;
            }
        }
        return dividedOperationLists;
    }

    public int insertOperationOfLedgerToOtherEditor(UpsertLedgerDto upsertLedgerDto) {
        OperationDto operationDto = new OperationDto(

        );
        return operationMapper.insertOperationToOtherEditor(upsertLedgerDto);
    }

    public int
}
