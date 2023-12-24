package com.pokemoney.hadoop.spark.dao;

import com.pokemoney.hadoop.spark.model.FundDimModel;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DorisFundMapper {
    @Insert("INSERT INTO fund_dim (fund_id, owner_id, name, balance, create_at, update_at, del_flag) VALUES (#{fundId}, #{ownerId}, #{name}, #{balance}, #{createAt}, #{updateAt}, #{delFlag})")
    int insertFundDim(FundDimModel fundDimModel);
}
