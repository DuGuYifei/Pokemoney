package com.pokemoney.hadoop.hbase.phoenix.dao.sqlprovider;

import com.pokemoney.hadoop.hbase.dto.filter.LedgerFilter;
import com.pokemoney.hadoop.hbase.dto.ledger.UpsertLedgerDto;
import com.pokemoney.hadoop.hbase.Constants;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * SQL Provider for Ledger in Phoenix
 */
public class LedgerSqlProvider {
    /**
     * Build sql for get ledger by row key.
     *
     * @param params the map of params
     * @return the string of sql
     */
    public String buildSqlGetLedgerByRowKey(Map<String, Object> params) {
        List<String> selectedFieldsName = (List<String>) params.get("selectedFieldsName");
        SQL sql = new SQL()
                .FROM(Constants.LEDGER_BOOK_TABLE)
                .WHERE("(region_id, user_id, ledger_id) = (#{regionId}, #{userId}, #{ledgerId}");
        if (selectedFieldsName == null || selectedFieldsName.isEmpty()) {
            sql.SELECT("*");
        } else {
            for (String selectedFieldName : selectedFieldsName) {
                sql.SELECT(selectedFieldName);
            }
        }
        return sql.toString();
    }

    /**
     * Build sql for get ledgers by user and filter.
     *
     * @param params the map of params
     * @return the string of sql
     */
    public String buildSqlGetLedgersByUserAndFilter(Map<String, Object> params) {
        List<String> selectedFieldsName = (List<String>) params.get("selectedFieldsName");
        LedgerFilter ledgerFilter = (LedgerFilter) params.get("ledgerFilter");
        SQL sql = new SQL()
                .FROM(Constants.LEDGER_BOOK_TABLE)
                .WHERE("region_id = #{regionId} AND user_id = #{userId}");
        for (String selectedFieldName : selectedFieldsName) {
            sql.SELECT(selectedFieldName);
        }
        if (ledgerFilter == null) {
            return sql.toString();
        }
        if (ledgerFilter.getMinBudget() != null) {
            sql.WHERE("ledger_info.budget >= #{ledgerFilter.minBudget}");
        }
        if (ledgerFilter.getMaxBudget() != null) {
            sql.WHERE("ledger_info.budget <= #{ledgerFilter.maxBudget}");
        }
        if (ledgerFilter.getOwner() != null) {
            sql.WHERE("ledger_info.owner = #{ledgerFilter.owner}");
        }
        if (ledgerFilter.getEditors() != null && ledgerFilter.getEditors().length > 0) {
            for (int i = 0; i < ledgerFilter.getEditors().length; i++) {
                sql.WHERE("#{ledgerFilter.editors[" + i + "]} = ANY(ledger_info.editors)");
            }
        }
        if (ledgerFilter.getMinCreateAt() != null) {
            sql.WHERE("ledger_info.create_at >= #{ledgerFilter.minCreateAt}");
        }
        if (ledgerFilter.getMaxCreateAt() != null) {
            sql.WHERE("ledger_info.create_at <= #{ledgerFilter.maxCreateAt}");
        }
        if (ledgerFilter.getMinUpdateAt() != null) {
            sql.WHERE("update_info.update_at >= #{ledgerFilter.minUpdateAt}");
        }
        if (ledgerFilter.getMaxUpdateAt() != null) {
            sql.WHERE("update_info.update_at <= #{ledgerFilter.maxUpdateAt}");
        }
        if (ledgerFilter.getDelFlag() != null) {
            sql.WHERE("update_info.del_flag = #{ledgerFilter.delFlag}");
        }
        return sql.toString();
    }

    /**
     * Build sql for update ledger by row key.
     *
     * @param upsertLedgerDto the update ledger dto {@link UpsertLedgerDto}
     * @return the string of sql
     */
    public String buildSqlUpdateLedgerByRowKey(UpsertLedgerDto upsertLedgerDto) {
        StringBuilder sql = new StringBuilder();
        sql.append("UPSERT INTO " + Constants.LEDGER_BOOK_TABLE + " (region_id, user_id, ledger_id");
        if (upsertLedgerDto.getName() != null) {
            sql.append(", ledger_info.name");
        }
        if (upsertLedgerDto.getBudget() != null) {
            sql.append(", ledger_info.budget");
        }
        if (upsertLedgerDto.getOwner() != null) {
            sql.append(", ledger_info.owner");
        }
        if (upsertLedgerDto.getEditors() != null) {
            sql.append(", ledger_info.editors");
        }
        if (upsertLedgerDto.getCreateAt() != null) {
            sql.append(", ledger_info.create_at");
        }
        if (upsertLedgerDto.getUpdateAt() != null) {
            sql.append(", update_info.update_at");
        }
        if (upsertLedgerDto.getDelFlag() != null) {
            sql.append(", update_info.del_flag");
        }
        sql.append(") VALUES (#{regionId}, #{userId}, #{ledgerId}");
        if (upsertLedgerDto.getName() != null) {
            sql.append(", #{name}");
        }
        if (upsertLedgerDto.getBudget() != null) {
            sql.append(", #{budget}");
        }
        if (upsertLedgerDto.getOwner() != null) {
            sql.append(", #{owner}");
        }
        if (upsertLedgerDto.getEditors() != null) {
            sql.append(", #{editors, jdbcType=ARRAY, typeHandler=org.apache.ibatis.type.ArrayTypeHandler}");
        }
        if (upsertLedgerDto.getCreateAt() != null) {
            sql.append(", #{createAt}");
        }
        if (upsertLedgerDto.getUpdateAt() != null) {
            sql.append(", #{updateAt}");
        }
        if (upsertLedgerDto.getDelFlag() != null) {
            sql.append(", #{delFlag}");
        }
        sql.append(")");
        return sql.toString();
    }
}
