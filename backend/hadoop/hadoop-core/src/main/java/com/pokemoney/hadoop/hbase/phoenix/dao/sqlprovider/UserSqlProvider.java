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
                .WHERE("(region_id, user_id) = (#{regionId}, #{userId}");
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
        if (upsertUserDto.getFundInfo() != null) {
            if (upsertUserDto.getFundInfo().getFundIds() != null) {
                sql.append(", fund_info.funds");
            }
            if (upsertUserDto.getFundInfo().getDelFundIds() != null) {
                sql.append(", fund_info.del_funds");
            }
        }
        if (upsertUserDto.getLedgerInfo() != null) {
            if (upsertUserDto.getLedgerInfo().getLedgerIds() != null) {
                sql.append(", ledger_info.ledgers");
            }
            if (upsertUserDto.getLedgerInfo().getDelLedgerIds() != null) {
                sql.append(", ledger_info.del_ledgers");
            }
        }
        if (upsertUserDto.getAppInfo() != null) {
            if (upsertUserDto.getAppInfo().getJsonCategories() != null) {
                sql.append(", app_info.jsonCategories");
            }
            if (upsertUserDto.getAppInfo().getJsonSubcategories() != null) {
                sql.append(", app_info.jsonSubcategories");
            }
        }
        sql.append(") VALUES (#{regionId}, #{userId}");
        if (upsertUserDto.getName() != null) {
            sql.append(", #{name}");
        }
        if (upsertUserDto.getEmail() != null) {
            sql.append(", #{email}");
        }
        if (upsertUserDto.getFundInfo() != null) {
            if (upsertUserDto.getFundInfo().getFundIds() != null) {
                sql.append(", #{fundInfo.fundIds}");
            }
            if (upsertUserDto.getFundInfo().getDelFundIds() != null) {
                sql.append(", #{fundInfo.delFundIds}");
            }
        }
        if (upsertUserDto.getLedgerInfo() != null) {
            if (upsertUserDto.getLedgerInfo().getLedgerIds() != null) {
                sql.append(", #{ledgerInfo.ledgerIds}");
            }
            if (upsertUserDto.getLedgerInfo().getDelLedgerIds() != null) {
                sql.append(", #{ledgerInfo.delLedgerIds}");
            }
        }
        if (upsertUserDto.getAppInfo() != null) {
            if (upsertUserDto.getAppInfo().getJsonCategories() != null) {
                sql.append(", #{appInfo.jsonCategories}");
            }
            if (upsertUserDto.getAppInfo().getJsonSubcategories() != null) {
                sql.append(", #{appInfo.jsonSubcategories}");
            }
        }
        sql.append(")");
        return sql.toString();
    }
}
