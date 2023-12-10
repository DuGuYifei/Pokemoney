package com.pokemoney.hadoop.hbase.phoenix.dao;

import com.pokemoney.hadoop.hbase.dto.transaction.UpsertTransactionDto;
import com.pokemoney.hadoop.hbase.phoenix.dao.sqlprovider.TransactionSqlProvider;
import com.pokemoney.hadoop.hbase.phoenix.model.TransactionModel;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * DAO for transaction in Phoenix
 */
@Mapper
public interface TransactionMapper {
    /**
     * Get transaction by transaction id.
     *
     * @param tableName             table name
     * @param regionId              region id
     * @param userId                user id
     * @param reverseTransactionId  reverse transaction id
     * @param selectedFieldsName    the selected fields name
     * @return transaction model
     */
    @Results(id = "transactionResultMap", value = {
            @Result(property = "regionId", column = "region_id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "ledgerId", column = "ledger_id"),
            @Result(property = "reverseTransactionId", column = "reverse_transaction_id"),
            @Result(property = "transactionInfo.id", column = "transaction_id"),
            @Result(property = "transactionInfo.money", column = "money"),
            @Result(property = "transactionInfo.typeId", column = "type_id"),
            @Result(property = "transactionInfo.relevantEntity", column = "relevant_entity"),
            @Result(property = "transactionInfo.comment", column = "comment"),
            @Result(property = "transactionInfo.fundId", column = "fund_id"),
            @Result(property = "transactionInfo.categoryId", column = "category_id"),
            @Result(property = "transactionInfo.subcategoryId", column = "subcategory_id"),
            @Result(property = "transactionInfo.ledgerId", column = "ledger_id"),
            @Result(property = "transactionInfo.happenAt", column = "happen_at"),
            @Result(property = "updateBy", column = "update_by"),
            @Result(property = "updateInfo.updateAt", column = "update_at"),
            @Result(property = "updateInfo.delFlag", column = "del_flag")
    })
    @SelectProvider(type = TransactionSqlProvider.class, method = "buildSqlForUserGetTransactionByTransactionId")
    TransactionModel getTransactionByRowKeyAndTableName(@Param("tableName") String tableName, @Param("regionId") Integer regionId, @Param("ledgerId") Long ledgerId, @Param("userId") Long userId, @Param("reverseTransactionId") Long reverseTransactionId, @Param("selectedFieldsName") List<String> selectedFieldsName);

    /**
     * Insert transaction.
     *
     * @param upsertTransactionDto insert transaction dto {@link UpsertTransactionDto}
     * @return the number of rows affected
     */
    @Insert("UPSERT INTO #{tableName} (region_id, user_id, ledger_id_rk, reverse_transaction_id, transaction_info.transaction_id, transaction_info.money, transaction_info.type_id, transaction_info.relevant_entity, transaction_info.comment, transaction_info.fund_id, transaction_info.category_id, transaction_info.subcategory_id, transaction_info.ledger_id, transaction_info.happen_at, update_info.update_by, update_info.update_at, update_info.del_flag) VALUES (#{regionId}, #{userId}, #{ledgerId}, #{reverseTransactionId}, #{transactionId}, #{money}, #{typeId}, #{relevantEntity}, #{comment}, #{fundId}, #{categoryId}, #{subcategoryId}, #{ledgerId}, #{happenAt}, {#updateBy}, #{updateAt}, #{delFlag})")
    int insertTransaction(UpsertTransactionDto upsertTransactionDto);

    /**
     * Update transaction.
     *
     * @param upsertTransactionDto update transaction dto {@link UpsertTransactionDto}
     * @return the number of rows affected
     */
    @UpdateProvider(type = TransactionSqlProvider.class, method = "buildSqlUpdateTransaction")
    int updateTransaction(UpsertTransactionDto upsertTransactionDto);
}
