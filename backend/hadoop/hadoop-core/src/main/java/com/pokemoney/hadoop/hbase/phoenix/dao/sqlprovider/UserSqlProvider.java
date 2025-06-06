package com.pokemoney.hadoop.hbase.phoenix.dao.sqlprovider;

import com.pokemoney.hadoop.hbase.Constants;
import com.pokemoney.hadoop.hbase.dto.user.UpsertUserDto;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * SQL Provider for User in Phoenix
 */
public class UserSqlProvider {
    /**
     * Build sql for get user by row key.
     *
     * @param params the map of params
     * @return the string of sql
     */
    public String buildSqlGetUserByRowKey(Map<String, Object> params) {
        List<String> selectedFieldsName = (List<String>) params.get("selectedFieldsName");
        SQL sql = new SQL()
                .FROM(Constants.USER_TABLE)
                .WHERE("(region_id, user_id) = (#{regionId}, #{userId})");
        if (selectedFieldsName == null || selectedFieldsName.isEmpty()) {
            sql.SELECT("region_id, user_id, user_info.name, user_info.email, user_info.update_at, fund_info.funds, fund_info.del_funds, ledger_info.ledgers, ledger_info.del_ledgers, app_info.categories, app_info.subcategories, notifications.new_notify");
        } else {
            for (String selectedFieldName : selectedFieldsName) {
                sql.SELECT(selectedFieldName);
            }
        }
        return sql.toString();
    }

    /**
     * Build sql for update user.
     *
     * @param upsertUserDto the user dto {@link UpsertUserDto}
     * @return the string of sql
     */
    public String buildSqlUpdateUser(UpsertUserDto upsertUserDto) {
        StringBuilder sql = new StringBuilder("UPSERT INTO " + Constants.USER_TABLE + " (region_id, user_id");
        if (upsertUserDto.getName() != null) {
            sql.append(", user_info.name");
        }
        if (upsertUserDto.getEmail() != null) {
            sql.append(", user_info.email");
        }
        if (upsertUserDto.getUpdateUserInfoAt() != null) {
            sql.append(", user_info.update_at");
        }
        if (upsertUserDto.getFundInfo() != null) {
            if (upsertUserDto.getFundInfo().getFunds() != null && !upsertUserDto.getFundInfo().getFunds().isEmpty()) {
                sql.append(", fund_info.funds");
            }
            if (upsertUserDto.getFundInfo().getDelFunds() != null && !upsertUserDto.getFundInfo().getDelFunds().isEmpty()) {
                sql.append(", fund_info.del_funds");
            }
        }
        if (upsertUserDto.getLedgerInfo() != null) {
            if (upsertUserDto.getLedgerInfo().getLedgers() != null && !upsertUserDto.getLedgerInfo().getLedgers().isEmpty()) {
                sql.append(", ledger_info.ledgers");
            }
            if (upsertUserDto.getLedgerInfo().getDelLedgers() != null && !upsertUserDto.getLedgerInfo().getDelLedgers().isEmpty()) {
                sql.append(", ledger_info.del_ledgers");
            }
        }
        if (upsertUserDto.getAppInfo() != null) {
            if (upsertUserDto.getAppInfo().getJsonCategories() != null) {
                sql.append(", app_info.categories");
            }
            if (upsertUserDto.getAppInfo().getJsonSubcategories() != null) {
                sql.append(", app_info.subcategories");
            }
        }
        if (upsertUserDto.getNotificationJson() != null) {
            sql.append(", notifications.new_notify");
        }
        sql.append(") VALUES (#{regionId}, #{userId}");
        if (upsertUserDto.getName() != null) {
            sql.append(", #{name}");
        }
        if (upsertUserDto.getEmail() != null) {
            sql.append(", #{email}");
        }
        if (upsertUserDto.getUpdateUserInfoAt() != null) {
            sql.append(", #{updateUserInfoAt}");
        }
        if (upsertUserDto.getFundInfo() != null) {
            if (upsertUserDto.getFundInfo().getFunds() != null && !upsertUserDto.getFundInfo().getFunds().isEmpty()) {
                sql.append(", #{fundInfo.funds, typeHandler = com.pokemoney.hadoop.hbase.phoenix.handler.StringListToArrayTypeHandler}");
            }
            if (upsertUserDto.getFundInfo().getDelFunds() != null && !upsertUserDto.getFundInfo().getDelFunds().isEmpty()) {
                sql.append(", #{fundInfo.delFunds, typeHandler = com.pokemoney.hadoop.hbase.phoenix.handler.StringListToArrayTypeHandler}");
            }
        }
        if (upsertUserDto.getLedgerInfo() != null) {
            if (upsertUserDto.getLedgerInfo().getLedgers() != null && !upsertUserDto.getLedgerInfo().getLedgers().isEmpty()) {
                sql.append(", #{ledgerInfo.ledgers, typeHandler = com.pokemoney.hadoop.hbase.phoenix.handler.StringListToArrayTypeHandler}");
            }
            if (upsertUserDto.getLedgerInfo().getDelLedgers() != null && !upsertUserDto.getLedgerInfo().getDelLedgers().isEmpty()) {
                sql.append(", #{ledgerInfo.delLedgers, typeHandler = com.pokemoney.hadoop.hbase.phoenix.handler.StringListToArrayTypeHandler}");
            }
        }
        if (upsertUserDto.getAppInfo() != null) {
            if (upsertUserDto.getAppInfo().getJsonCategories() != null && !upsertUserDto.getAppInfo().getJsonCategories().isEmpty()) {
                sql.append(", #{appInfo.jsonCategories}");
            }
            if (upsertUserDto.getAppInfo().getJsonSubcategories() != null && !upsertUserDto.getAppInfo().getJsonSubcategories().isEmpty()) {
                sql.append(", #{appInfo.jsonSubcategories}");
            }
        }
        if (upsertUserDto.getNotificationJson() != null) {
            sql.append(", #{notificationJson}");
        }
        sql.append(")");
        return sql.toString();
    }
}
