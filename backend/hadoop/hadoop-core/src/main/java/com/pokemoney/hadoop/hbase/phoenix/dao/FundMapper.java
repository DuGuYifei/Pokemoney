package com.pokemoney.hadoop.hbase.phoenix.dao;

import com.pokemoney.hadoop.hbase.Constants;
import com.pokemoney.hadoop.hbase.dto.filter.FundFilter;
import com.pokemoney.hadoop.hbase.dto.fund.UpsertFundDto;
import com.pokemoney.hadoop.hbase.phoenix.dao.sqlprovider.FundSqlProvider;
import com.pokemoney.hadoop.hbase.phoenix.handler.LongListToArrayTypeHandler;
import com.pokemoney.hadoop.hbase.phoenix.model.FundModel;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

/**
 * DAO for Fund in Phoenix.
 */
@Mapper
public interface FundMapper {
    /**
     * Get fund by fund id.
     *
     * @param regionId region id
     * @param userId user id
     * @param fundId fund id
     * @param selectedFieldsName the selected fields name
     * @return fund model
     */
    @Results(id = "fundResultMap", value = {
            @Result(property = "regionId", column = "region_id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "fundId", column = "fund_id"),
            @Result(property = "fundInfo.name", column = "name"),
            @Result(property = "fundInfo.balance", column = "balance"),
            @Result(property = "fundInfo.editors", column = "editors", jdbcType = JdbcType.ARRAY, typeHandler = LongListToArrayTypeHandler.class),
            @Result(property = "fundInfo.owner", column = "owner"),
            @Result(property = "fundInfo.createAt", column = "create_at"),
            @Result(property = "updateInfo.updateAt", column = "update_at"),
            @Result(property = "updateInfo.delFlag", column = "del_flag")
    })
    @SelectProvider(type = FundSqlProvider.class, method = "buildSqlGetFundByRowKey")
    FundModel getFundByRowKey(@Param("regionId") Integer regionId, @Param("userId") Long userId, @Param("fundId") Long fundId, @Param("selectedFieldsName") List<String> selectedFieldsName);

    /**
     * Get funds by filter.
     *
     * @param regionId region id
     * @param userId user id
     * @param fundFilter the fund filter {@link FundFilter}
     * @param selectedFieldsName the selected fields name
     * @return fund model list
     */
    @ResultMap("fundResultMap")
    @SelectProvider(type = FundSqlProvider.class, method = "buildSqlGetFundsByUserAndFilter")
    List<FundModel> getFundsByUserAndFilter(@Param("regionId") Integer regionId, @Param("userId") Long userId, @Param("fundFilter") FundFilter fundFilter, @Param("selectedFieldsName") List<String> selectedFieldsName);

    /**
     * Insert fund.
     *
     * @param upsertFundDto the fund dto {@link UpsertFundDto}
     * @return the number of rows affected by the insert
     */
    @Insert("UPSERT INTO " + Constants.FUND_TABLE + " (region_id, user_id, fund_id, fund_info.id, fund_info.name, fund_info.balance, fund_info.owner, fund_info.editors, fund_info.create_at, update_info.update_at, update_info.del_flag) VALUES (#{regionId}, #{userId}, #{fundId}, #{fundId}, #{name}, #{balance}, #{owner}, #{editors, jdbcType=ARRAY,typeHandler=com.pokemoney.hadoop.hbase.phoenix.handler.LongListToArrayTypeHandler}, #{createAt}, #{updateAt}, #{delFlag})")
    int insertFund(UpsertFundDto upsertFundDto);

    /**
     * Update fund.
     *
     * @param upsertFundDto the fund dto {@link UpsertFundDto}
     * @return the number of rows affected by the update
     */
    @UpdateProvider(type = FundSqlProvider.class, method = "buildSqlUpdateFundByRowKey")
    int updateFundByRowKey(UpsertFundDto upsertFundDto);
}
