package com.pokemoney.hadoop.hbase.phoenix.dao;

import com.pokemoney.hadoop.hbase.dto.filter.LedgerFilter;
import com.pokemoney.hadoop.hbase.dto.ledger.UpsertLedgerDto;
import com.pokemoney.hadoop.hbase.Constants;
import com.pokemoney.hadoop.hbase.phoenix.dao.sqlprovider.LedgerSqlProvider;
import com.pokemoney.hadoop.hbase.phoenix.handler.LongListToArrayTypeHandler;
import com.pokemoney.hadoop.hbase.phoenix.model.LedgerModel;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.StringTypeHandler;

import java.util.List;

/**
 * DAO for ledger in Phoenix
 */
@Mapper
public interface LedgerMapper {
    /**
     * Get ledger by ledger id.
     *
     * @param regionId       region id
     * @param userId         user id
     * @param ledgerId ledger id
     * @param selectedFieldsName the selected fields name
     * @return ledger model
     */
    @Results(id = "ledgerResultMap", value = {
            @Result(property = "regionId", column = "region_id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "ledgerId", column = "ledger_id"),
            @Result(property = "ledgerInfo.name", column = "name"),
            @Result(property = "ledgerInfo.budget", column = "budget"),
            @Result(property = "ledgerInfo.owner", column = "owner"),
            @Result(property = "ledgerInfo.editors", column = "editors", jdbcType = JdbcType.ARRAY, typeHandler = LongListToArrayTypeHandler.class),
            @Result(property = "ledgerInfo.createAt", column = "create_at"),
            @Result(property = "updateInfo.updateAt", column = "update_at"),
            @Result(property = "updateInfo.delFlag", column = "del_flag")
    })
    @SelectProvider(type = LedgerSqlProvider.class, method = "buildSqlGetLedgerByRowKey")
    LedgerModel getLedgerByRowKey(@Param("regionId") Integer regionId, @Param("userId") Long userId, @Param("ledgerId") Long ledgerId, @Param("selectedFieldsName") List<String> selectedFieldsName);

    /**
     * Get ledgers by filter.
     *
     * @param regionId     region id
     * @param userId       user id
     * @param ledgerFilter the ledger filter {@link LedgerFilter}
     * @param selectedFieldsName the selected fields name
     * @return ledger model list
     */
    @ResultMap("ledgerResultMap")
    @SelectProvider(type = LedgerSqlProvider.class, method = "buildSqlGetLedgersByUserAndFilter")
    List<LedgerModel> getLedgersByUserAndFilter(@Param("regionId") Integer regionId, @Param("userId") Long userId, @Param("ledgerFilter") LedgerFilter ledgerFilter, @Param("selectedFieldsName") List<String> selectedFieldsName);

    /**
     * Insert ledger.
     *
     * @param upsertLedgerDto insert ledger dto {@link UpsertLedgerDto}
     * @return the number of rows affected
     */
    @Insert("UPSERT INTO " + Constants.LEDGER_BOOK_TABLE + " (region_id, user_id, ledger_id, ledger_info.id, ledger_info.name, ledger_info.budget, ledger_info.owner, ledger_info.editors, ledger_info.create_at, update_info.update_at, update_info.del_flag) VALUES (#{regionId}, #{userId}, #{ledgerId}, #{ledgerId}, #{name}, #{budget}, #{owner}, #{editors, jdbcType=ARRAY, typeHandler=com.pokemoney.hadoop.hbase.phoenix.handler.LongListToArrayTypeHandler}, #{createAt}, #{updateAt}, #{delFlag})")
    int insertLedger(UpsertLedgerDto upsertLedgerDto);

    /**
     * Update ledger.
     *
     * @param upsertLedgerDto update ledger dto {@link UpsertLedgerDto}
     * @return the number of rows affected
     */
    @UpdateProvider(type = LedgerSqlProvider.class, method = "buildSqlUpdateLedgerByRowKey")
    int updateLedgerByRowKey(UpsertLedgerDto upsertLedgerDto);
}
