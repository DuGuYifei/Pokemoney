package com.pokemoney.hadoop.spark.dao;

import com.pokemoney.hadoop.spark.model.LedgerDimModel;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * The transaction fact mapper.
 */
@Mapper
public interface DorisLedgerMapper {
    @Insert("INSERT INTO ledger_dim (ledger_id, owner_id, name, budget, create_at, update_at, del_flag) VALUES (#{ledgerId}, #{ownerId}, #{name}, #{budget}, #{createAt}, #{updateAt}, #{delFlag})")
    int insertLedger(LedgerDimModel ledgerDimModel);
}
