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
            @Result(property = "reverseTransactionId", column = "reverse_transaction_id"),
            @Result(property = "transactionInfo.id", column = "transaction_info.transaction_id"),
            @Result(property = "transactionInfo.money", column = "transaction_info.money"),
            @Result(property = "transactionInfo.typeId", column = "transaction_info.type_id"),
            @Result(property = "transactionInfo.relevantEntity", column = "transaction_info.relevant_entity"),
            @Result(property = "transactionInfo.comment", column = "transaction_info.comment"),
            @Result(property = "transactionInfo.fundId", column = "transaction_info.fund_id"),
            @Result(property = "transactionInfo.categoryId", column = "transaction_info.category_id"),
            @Result(property = "transactionInfo.subcategoryId", column = "transaction_info.subcategory_id"),
            @Result(property = "transactionInfo.ledgerId", column = "transaction_info.ledger_id"),
            @Result(property = "transactionInfo.happenAt", column = "transaction_info.happen_at"),
            @Result(property = "updateBy", column = "update_info.update_by"),
            @Result(property = "updateInfo.updateAt", column = "update_info.update_at"),
            @Result(property = "updateInfo.delFlag", column = "update_info.del_flag")
    })
    @SelectProvider(type = TransactionSqlProvider.class, method = "buildSqlForUserGetTransactionByTransactionId")
    TransactionModel getTransactionByTransactionId(@Param("tableName") String tableName, @Param("regionId") Integer regionId, @Param("userId") Long userId, @Param("reverseTransactionId") String reverseTransactionId, @Param("selectedFieldsName") List<String> selectedFieldsName);

    /**
     * Insert transaction.
     *
     * @param upsertTransactionDto insert transaction dto {@link UpsertTransactionDto}
     * @return the number of rows affected
     */
    @Insert("UPSERT INTO #{tableName} (region_id, user_id, reverse_transaction_id, transaction_info.transaction_id, transaction_info.money, transaction_info.type_id, transaction_info.relevant_entity, transaction_info.comment, transaction_info.fund_id, transaction_info.category_id, transaction_info.subcategory_id, transaction_info.ledger_id, transaction_info.happen_at, update_info.update_at, update_info.del_flag) VALUES (#{regionId}, #{userId}, #{reverseTransactionId}, #{transactionId}, #{money}, #{typeId}, #{relevantEntity}, #{comment}, #{fundId}, #{categoryId}, #{subcategoryId}, #{ledgerId}, #{happenAt}, #{updateAt}, #{delFlag})")
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
