package com.pokemoney.hadoop.hbase.phoenix.dao.sqlprovider;

import com.pokemoney.hadoop.hbase.dto.filter.FundFilter;
import com.pokemoney.hadoop.hbase.dto.fund.MutateFundDto;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;

/**
 * SQL Provider for Fund in Phoenix
 */
public class FundSqlProvider {
    /**
     * Build sql for user - getFundByFundId.
     */
    public String buildSqlForUserGetFundByFundId(String regionId, String userId, String fundIdRowKey,  List<String> selectedFieldsName) {
        SQL sql = new SQL()
                .FROM("t_funds")
                .WHERE("region_id = #{regionId} AND user_id = #{userId} AND fund_id = #{fundIdRowKey}");
        for (String selectedFieldName : selectedFieldsName) {
            sql.SELECT(selectedFieldName);
        }
        return sql.toString();
    }

    /**
     * Build sql for user - getFundsByFilter.
     *
     * @param fundFilter the fund filter
     * @return the string of sql
     */
    public String buildSqlForUserGetFundsByFilter(String regionId, String userId, FundFilter fundFilter, List<String> selectedFieldsName) {
        SQL sql = new SQL()
                .FROM("t_funds")
                .WHERE("region_id = #{regionId} AND user_id = #{userId}");
        for (String selectedFieldName : selectedFieldsName) {
            sql.SELECT(selectedFieldName);
        }
        if (fundFilter == null) {
            return sql.toString();
        }
        if (fundFilter.getMinBalance() != null) {
            sql.WHERE("fund_info.balance >= #{minBalance}");
        }
        if (fundFilter.getMaxBalance() != null) {
            sql.WHERE("fund_info.balance <= #{maxBalance}");
        }
        if (fundFilter.getOwner() != null) {
            sql.WHERE("fund_info.owner = #{owner}");
        }
        if (fundFilter.getEditors() != null && fundFilter.getEditors().length > 0) {
            throw new UnsupportedOperationException("You don't have permission to query by editors.");
        }
        if (fundFilter.getMinCreateAt() != null) {
            sql.WHERE("fund_info.create_at >= #{minCreateAt}");
        }
        if (fundFilter.getMaxCreateAt() != null) {
            sql.WHERE("fund_info.create_at <= #{maxCreateAt}");
        }
        if (fundFilter.getMinUpdateAt() != null) {
            sql.WHERE("update_info.update_at >= #{minUpdateAt}");
        }
        if (fundFilter.getMaxUpdateAt() != null) {
            sql.WHERE("update_info.update_at <= #{maxUpdateAt}");
        }
        return sql.toString();
    }

    /**
     * Build sql for user - buildSqlForUserUpdateFund
     *
     * @param regionId region id
     * @param userId user id
     * @param mutateFundDto the fund dto {@link MutateFundDto}
     * @return the string of sql
     */
    public String buildSqlForUserUpdateFund(String regionId, String userId, MutateFundDto mutateFundDto) {
        SQL sql = new SQL()
                .UPDATE("t_funds")
                .WHERE("region_id = #{regionId} AND user_id = #{userId} AND fund_id = #{mutateFundDto.fundId}");
        if (mutateFundDto.getName() != null) {
            sql.SET("fund_info.name = #{mutateFundDto.name}");
        }
        if (mutateFundDto.getBalance() != null) {
            sql.SET("fund_info.balance = #{mutateFundDto.balance}");
        }
        if (mutateFundDto.getOwner() != null) {
            sql.SET("fund_info.owner = #{mutateFundDto.owner}");
        }
        if (mutateFundDto.getEditors() != null && mutateFundDto.getEditors().length > 0) {
            sql.SET("fund_info.editors = #{mutateFundDto.editors}");
        }
        if (mutateFundDto.getUpdateAt() != null) {
            sql.SET("update_info.update_at = #{mutateFundDto.updateAt}");
        }
        return sql.toString();
    }
}