package com.pokemoney.hadoop.spark.dao;

import com.pokemoney.hadoop.spark.model.TransactionAnalysisModel;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

/**
 * The transaction analysis mapper.
 */
@Mapper
public interface DorisTransactionAnalysisMapper {
    @Insert("INSERT INTO transaction_example_fact (id, sum_money, category_id, type_id, time_analysis_id) VALUES (#{id}, #{sumMoney}, #{categoryId}, #{typeId}, #{timeAnalysisId})")
    int insertTransactionAnalysis(TransactionAnalysisModel transactionAnalysisModel);
}
