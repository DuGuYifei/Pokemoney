package com.pokemoney.hadoop.spark.dao;

import com.pokemoney.hadoop.spark.model.TransactionFactModel;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * The transaction fact mapper.
 */
@Mapper
public interface DorisTransactionMapper {
    @Insert("INSERT INTO transaction_fact (id, money, fund_id, ledger_id, type_id, category_id, happen_at, happen_time_id, happen_time_analysis_id, update_by, update_at, update_time_id, update_time_analysis_id, del_flag) VALUES (#{id}, #{money}, #{fundId}, #{ledgerId}, #{typeId}, #{categoryId}, #{happenAt}, #{happenTimeId}, #{happenTimeAnalysisId}, #{updateBy}, #{updateAt}, #{updateTimeId}, #{updateTimeAnalysisId}, #{delFlag})")
    int insertTransaction(TransactionFactModel transactionFactModel);
}
