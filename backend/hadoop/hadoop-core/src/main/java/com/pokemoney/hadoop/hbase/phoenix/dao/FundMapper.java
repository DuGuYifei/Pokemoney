package com.pokemoney.hadoop.hbase.phoenix.dao;

import com.pokemoney.hadoop.hbase.dto.filter.FundFilter;
import com.pokemoney.hadoop.hbase.dto.fund.MutateFundDto;
import com.pokemoney.hadoop.hbase.phoenix.dao.sqlprovider.FundSqlProvider;
import com.pokemoney.hadoop.hbase.phoenix.model.FundModel;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * The fund mapper of mybatis.
 */
@Mapper
public interface FundMapper {
    /**
     * Get fund by fund id.
     *
     * @param regionId region id
     * @param userId user id
     * @param fundIdRowKey fund id
     * @return fund model
     */
    @Results(id = "fundResultMap", value = {
            @Result(property = "regionId", column = "region_id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "fundId", column = "fund_id"),
            @Result(property = "fundInfo.fundId", column = "fund_id"),
            @Result(property = "fundInfo.name", column = "name"),
            @Result(property = "fundInfo.balance", column = "balance"),
            @Result(property = "fundInfo.owner", column = "owner"),
            @Result(property = "fundInfo.editors", column = "editors"),
            @Result(property = "fundInfo.createTime", column = "create_time"),
            @Result(property = "fundInfo.updateTime", column = "update_time"),
            @Result(property = "fundInfo.deleteTime", column = "delete_time"),
            @Result(property = "updateColumnFamily.updateAt", column = "update_at"),
            @Result(property = "updateColumnFamily.delFlag", column = "del_flag")
    })
    @SelectProvider(type = FundSqlProvider.class, method = "buildSqlForUserGetFundByFundId")
    FundModel getFundByFundId(String regionId, String userId, String fundIdRowKey, List<String> selectedFieldsName);

    /**
     * Get funds by filter.
     *
     * @param fundFilter the fund filter {@link FundFilter}
     * @return fund model list
     */
    @ResultMap("fundResultMap")
    @SelectProvider(type = FundSqlProvider.class, method = "buildSqlForUserGetFundsByFilter")
    List<FundModel> getFundsByFilter(String regionId, String userId, FundFilter fundFilter, List<String> selectedFieldsName);

    /**
     * Insert fund.
     *
     * @param mutateFundDto the fund dto {@link MutateFundDto}
     * @return the number of rows affected by the insert
     */
    @Insert("UPSERT INTO t_funds (region_id, user_id, fund_id, " +
            "fund_info.id, fund_info.name, fund_info.balance, fund_info.owner, fund_info.editors, fund_info.create_at, " +
            "update_info.update_at, update_info.del_flag) " +
            "VALUES (#{regionId}, #{userId}, #{mutateFundDto.fundId}, " +
            "#{mutateFundDto.fundId}, #{mutateFundDto.name}, #{mutateFundDto.balance}, #{mutateFundDto.owner}, #{mutateFundDto.editors}, #{mutateFundDto.updateAt}, " +
            "#{mutateFundDto.updateAt}, 0})")
    int insertFund(String regionId, String userId, MutateFundDto mutateFundDto);

    /**
     * Update fund.
     *
     * @param mutateFundDto the fund dto {@link MutateFundDto}
     * @return the number of rows affected by the update
     */
    @UpdateProvider(type = FundSqlProvider.class, method = "buildSqlForUserUpdateFund")
    int updateFund(String regionId, String userId, MutateFundDto mutateFundDto);

    /**
     * Update del flag of fund.
     *
     * @param regionId region id
     * @param userId user id
     * @param fundIdRowKey fund id
     * @param delFlag del flag
     * @return the number of rows affected by the update
     */
    @Update("UPDATE t_funds SET update_info.del_flag = #{delFlag} WHERE region_id = #{regionId} AND user_id = #{userId} AND fund_id = #{fundIdRowKey}")
    int updateDelFlagOfFund(String regionId, String userId, String fundIdRowKey, Integer delFlag);
}
