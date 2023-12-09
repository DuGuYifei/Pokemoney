package com.pokemoney.hadoop.hbase.phoenix.dao.sqlprovider;

import com.pokemoney.hadoop.hbase.dto.transaction.UpsertTransactionDto;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

/**
 * SQL provider for transaction.
 */
public class TransactionSqlProvider {
    /**
     * Build sql for user get transaction by transaction id.
     *
     * @param params the map of params
     * @return the string of sql
     */
    public String buildSqlForUserGetTransactionByTransactionId(Map<String, Object> params) {
        List<String> selectedFieldsName = (List<String>) params.get("selectedFieldsName");
        SQL sql = new SQL()
                .FROM("#{tableName}")
                .WHERE("(region_id, user_id, ledger_id_rk, transaction_id) = (#{regionId}, #{userId}, #{ledgerId}, #{transactionId}");
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
     * Build sql for user update transaction.
     *
     * @param upsertTransactionDto update transaction dto {@link UpsertTransactionDto}
     * @return the string of sql
     */
    public String buildSqlUpdateTransaction(UpsertTransactionDto upsertTransactionDto) {
        StringBuilder sql = new StringBuilder("UPSERT INTO #{tableName} (region_id, user_id, ledger_id_rk, reverse_transaction_id");
        if (upsertTransactionDto.getMoney() != null) {
            sql.append(", transaction_info.money");
        }
        if (upsertTransactionDto.getTypeId() != null) {
            sql.append(", transaction_info.type_id");
        }
        if (upsertTransactionDto.getRelevantEntity() != null) {
            sql.append(", transaction_info.relevant_entity");
        }
        if (upsertTransactionDto.getComment() != null) {
            sql.append(", transaction_info.comment");
        }
        if (upsertTransactionDto.getFundId() != null) {
            sql.append(", transaction_info.fund_id");
        }
        if (upsertTransactionDto.getCategoryId() != null) {
            sql.append(", transaction_info.category_id");
        }
        if (upsertTransactionDto.getSubcategoryId() != null) {
            sql.append(", transaction_info.subcategory_id");
        }
        if (upsertTransactionDto.getLedgerId() != null) {
            sql.append(", transaction_info.ledger_id");
        }
        if (upsertTransactionDto.getHappenAt() != null) {
            sql.append(", transaction_info.happen_at");
        }
        if (upsertTransactionDto.getUpdateBy() != null) {
            sql.append(", update_info.update_by");
        }
        if (upsertTransactionDto.getUpdateAt() != null) {
            sql.append(", update_info.update_at");
        }
        if (upsertTransactionDto.getDelFlag() != null) {
            sql.append(", update_info.del_flag");
        }
        sql.append(") VALUES (#{regionId}, #{userId}, #{ledgerId} ,#{reverseTransactionId}");
        if (upsertTransactionDto.getMoney() != null) {
            sql.append(", #{money}");
        }
        if (upsertTransactionDto.getTypeId() != null) {
            sql.append(", #{typeId}");
        }
        if (upsertTransactionDto.getRelevantEntity() != null) {
            sql.append(", #{relevantEntity}");
        }
        if (upsertTransactionDto.getComment() != null) {
            sql.append(", #{comment}");
        }
        if (upsertTransactionDto.getFundId() != null) {
            sql.append(", #{fundId}");
        }
        if (upsertTransactionDto.getCategoryId() != null) {
            sql.append(", #{categoryId}");
        }
        if (upsertTransactionDto.getSubcategoryId() != null) {
            sql.append(", #{subcategoryId}");
        }
        if (upsertTransactionDto.getLedgerId() != null) {
            sql.append(", #{ledgerId}");
        }
        if (upsertTransactionDto.getHappenAt() != null) {
            sql.append(", #{happenAt}");
        }
        if (upsertTransactionDto.getUpdateBy() != null) {
            sql.append(", #{updateBy}");
        }
        if (upsertTransactionDto.getUpdateAt() != null) {
            sql.append(", #{updateAt}");
        }
        if (upsertTransactionDto.getDelFlag() != null) {
            sql.append(", #{delFlag}");
        }
        sql.append(")");
        return sql.toString();
    }
}
