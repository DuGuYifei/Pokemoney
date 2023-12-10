package com.pokemoney.hadoop.hbase.phoenix.dao;

import com.pokemoney.hadoop.hbase.Constants;
import com.pokemoney.hadoop.hbase.dto.operation.OperationDto;
import com.pokemoney.hadoop.hbase.phoenix.model.OperationModel;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * DAO for operation in Phoenix
 */
public interface OperationMapper {
    /**
     * Get operation which has bigger operation id than the given operation id.
     * @param regionId region id
     * @param userId user id
     * @param reverseOperationId operation id which is long.max_value - operation id
     * @return operation model list
     */
    @Results(
            id = "operationResultMap",
            value = {
                    @Result(property = "regionId", column = "region_id"),
                    @Result(property = "userId", column = "user_id"),
                    @Result(property = "reverseOperationId", column = "reverse_operation_id"),
                    @Result(property = "operationInfo.id", column = "operation_id"),
                    @Result(property = "operationInfo.targetTable", column = "target_table"),
                    @Result(property = "operationInfo.targetRowKey", column = "target_row_key"),
                    @Result(property = "updateAt", column = "update_at")
            }
    )
    @Select("SELECT operation_info.operation_id, operation_info.target_table, operation_info.target_row_key, MAX(update_info.update_at) FROM " + Constants.OPERATION_TABLE + " WHERE region_id = #{regionId} AND user_id = #{userId} AND reverse_operation_id < #{reverseOperationId} LIMIT #{limit} GROUP BY operation_info.operation_id, operation_info.target_table, operation_info.target_row_key")
    List<OperationModel> getOperationsLowerReverseIdDistinctTargetRowKeyWithLimit(@Param("regionId") Integer regionId, @Param("userId") Long userId, @Param("reverseOperationId") Long reverseOperationId, @Param("limit") int limit);

    /**
     * Get operation which has bigger operation id than the given operation id.
     *
     * @param regionId region id
     * @param userId user id
     * @param reverseOperationId operation id which is long.max_value - operation id
     * @return operation model list
     */
    @ResultMap("operationResultMap")
    @Select("SELECT operation_info.operation_id, operation_info.target_table, operation_info.target_row_key, MAX(update_info.update_at) FROM " + Constants.OPERATION_TABLE + " WHERE region_id = #{regionId} AND user_id = #{userId} AND reverse_operation_id < #{reverseOperationId} GROUP BY operation_info.operation_id, operation_info.target_table, operation_info.target_row_key")
    List<OperationModel> getOperationsLowerReverseIdDistinctTargetRowKey(@Param("regionId") Integer regionId, @Param("userId") Long userId, @Param("reverseOperationId") Long reverseOperationId);

    /**
     * Insert operation.
     *
     * @param operationDto operation dto
     * @return the number of rows affected
     */
    @Insert("UPSERT INTO " + Constants.OPERATION_TABLE + " (region_id, user_id, reverse_operation_id, operation_info.operation_id, operation_info.target_table, operation_info.target_row_key, update_at) VALUES (#{regionId}, #{userId}, #{reverseOperationId}, #{operationId}, #{targetTable}, #{targetRowKey}, #{updateAt})")
    int insertOperation(OperationDto operationDto);
}
