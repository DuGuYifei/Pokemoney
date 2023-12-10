package com.pokemoney.hadoop.hbase.phoenix.dao.sqlprovider;

import com.pokemoney.hadoop.hbase.Constants;
import com.pokemoney.hadoop.hbase.dto.filter.FundFilter;
import com.pokemoney.hadoop.hbase.dto.fund.UpsertFundDto;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * SQL Provider for Fund in Phoenix
 */
public class FundSqlProvider {
    /**
     * Build sql for get fund by row key.
     *
     * @param params the map of params
     * @return the string of sql
     */
    public String buildSqlGetFundByRowKey(Map<String, Object> params) {
        List<String> selectedFieldsName = (List<String>) params.get("selectedFieldsName");
        Integer regionId = (Integer) params.get("regionId");
        Long userId = (Long) params.get("userId");
        Long fundId = (Long) params.get("fundId");
        System.out.println(regionId);
        System.out.println(userId);
        System.out.println(fundId);
//        SQL sql = new SQL()
//                .FROM(Constants.FUND_TABLE)
//                .WHERE("(region_id, user_id, fund_id) = (#{regionId}, #{userId}, #{fundId})");
//        if (selectedFieldsName == null || selectedFieldsName.isEmpty()) {
//            sql.SELECT("*");
//        } else {
//            for (String selectedFieldName : selectedFieldsName) {
//                sql.SELECT(selectedFieldName);
//            }
//        }
//        System.out.println(sql.toString());
//        return sql.toString();
        StringBuilder sql = new StringBuilder("SELECT ");
        if (selectedFieldsName == null || selectedFieldsName.isEmpty()) {
            sql.append("region_id, user_id, fund_id, fund_info.name, fund_info.balance, fund_info.owner, fund_info.editors, fund_info.create_at, update_info.update_at, update_info.del_flag");
        } else {
            for (int i = 0; i < selectedFieldsName.size(); i++) {
                sql.append(selectedFieldsName.get(i));
                if (i != selectedFieldsName.size() - 1) {
                    sql.append(", ");
                }
            }
        }
        sql.append(" FROM " + Constants.FUND_TABLE + " WHERE (region_id,user_id,fund_id) = (#{regionId},#{userId},#{fundId})");
        return sql.toString();
    }

    /**
     * Build sql for get funds by user and filter.
     *
     * @param params the map of params
     * @return the string of sql
     */
    public String buildSqlGetFundsByUserAndFilter(Map<String, Object> params) {
        List<String> selectedFieldsName = (List<String>) params.get("selectedFieldsName");
        FundFilter fundFilter = (FundFilter) params.get("fundFilter");
        SQL sql = new SQL()
                .FROM(Constants.FUND_TABLE)
                .WHERE("region_id = #{regionId} AND user_id = #{userId}");
        if (selectedFieldsName == null || selectedFieldsName.isEmpty()) {
            sql.SELECT("*");
        } else {
            for (String selectedFieldName : selectedFieldsName) {
                sql.SELECT(selectedFieldName);
            }
        }
        if (fundFilter == null) {
            return sql.toString();
        }
        if (fundFilter.getMinBalance() != null) {
            sql.WHERE("fund_info.balance >= #{fundFilter.minBalance}");
        }
        if (fundFilter.getMaxBalance() != null) {
            sql.WHERE("fund_info.balance <= #{fundFilter.maxBalance}");
        }
        if (fundFilter.getOwner() != null) {
            sql.WHERE("fund_info.owner = #{fundFilter.owner}");
        }
        if (fundFilter.getEditors() != null) {
            for (int i = 0; i < fundFilter.getEditors().length; i++) {
                sql.WHERE("#{fundFilter.editors[" + i + "]} = ANY(fund_info.editors)");
            }
        }
        if (fundFilter.getMinCreateAt() != null) {
            sql.WHERE("fund_info.create_at >= #{fundFilter.minCreateAt}");
        }
        if (fundFilter.getMaxCreateAt() != null) {
            sql.WHERE("fund_info.create_at <= #{fundFilter.maxCreateAt}");
        }
        if (fundFilter.getMinUpdateAt() != null) {
            sql.WHERE("update_info.update_at >= #{fundFilter.minUpdateAt}");
        }
        if (fundFilter.getMaxUpdateAt() != null) {
            sql.WHERE("update_info.update_at <= #{fundFilter.maxUpdateAt}");
        }
        if (fundFilter.getDelFlag() != null) {
            sql.WHERE("update_info.del_flag = #{fundFilter.delFlag}");
        }
        return sql.toString();
    }

    /**
     * Build sql for update fund by row key.
     *
     * @param upsertFundDto the fund dto {@link UpsertFundDto}
     * @return the string of sql
     */
    public String buildSqlUpdateFundByRowKey(UpsertFundDto upsertFundDto) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPSERT INTO " + Constants.FUND_TABLE + " (region_id, user_id, fund_id");
        if (upsertFundDto.getName() != null) {
            sql.append(", fund_info.name");
        }
        if (upsertFundDto.getBalance() != null) {
            sql.append(", fund_info.balance");
        }
        if (upsertFundDto.getOwner() != null) {
            sql.append(", fund_info.owner");
        }
        if (upsertFundDto.getEditors() != null && !upsertFundDto.getEditors().isEmpty()) {
            sql.append(", fund_info.editors");
        }
        if (upsertFundDto.getCreateAt() != null) {
            sql.append(", fund_info.create_at");
        }
        if (upsertFundDto.getUpdateAt() != null) {
            sql.append(", update_info.update_at");
        }
        if (upsertFundDto.getDelFlag() != null) {
            sql.append(", update_info.del_flag");
        }
        sql.append(") VALUES (#{updateFundDto.regionId}, #{updateFundDto.userId}, #{updateFundDto.fundId}");
        if (upsertFundDto.getName() != null) {
            sql.append(", #{name}");
        }
        if (upsertFundDto.getBalance() != null) {
            sql.append(", #{balance}");
        }
        if (upsertFundDto.getOwner() != null) {
            sql.append(", #{owner}");
        }
        if (upsertFundDto.getEditors() != null && !upsertFundDto.getEditors().isEmpty()) {
            sql.append(", #{editors, jdbcType=ARRAY,typeHandler=com.pokemoney.hadoop.hbase.phoenix.handler.LongListToArrayTypeHandler}");
        }
        if (upsertFundDto.getCreateAt() != null) {
            sql.append(", #{createAt}");
        }
        if (upsertFundDto.getUpdateAt() != null) {
            sql.append(", #{updateAt}");
        }
        if (upsertFundDto.getDelFlag() != null) {
            sql.append(", #{delFlag}");
        }
        sql.append(")");
        return sql.toString();
    }
}