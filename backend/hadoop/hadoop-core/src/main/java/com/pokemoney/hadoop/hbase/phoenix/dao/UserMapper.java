package com.pokemoney.hadoop.hbase.phoenix.dao;

import com.pokemoney.hadoop.hbase.Constants;
import com.pokemoney.hadoop.hbase.dto.user.UpsertUserDto;
import com.pokemoney.hadoop.hbase.phoenix.dao.sqlprovider.UserSqlProvider;
import com.pokemoney.hadoop.hbase.phoenix.model.UserModel;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.ArrayTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

/**
 * DAO for User in Phoenix.
 */
@Mapper
public interface UserMapper {
    /**
     * Get user by user id.
     *
     * @param regionId the region id
     * @param userId the user id
     * @param selectedFieldsName the selected fields name
     * @return user model
     */
    @Results(id = "userResultMap", value = {
            @Result(property = "regionId", column = "region_id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "userInfo.name", column = "user_info.name"),
            @Result(property = "userInfo.email", column = "user_info.email"),
            @Result(property = "fundInfo.funds", column = "fund_info.funds", jdbcType = JdbcType.ARRAY, typeHandler = ArrayTypeHandler.class),
            @Result(property = "fundInfo.delFunds", column = "fund_info.del_funds", jdbcType = JdbcType.ARRAY, typeHandler = ArrayTypeHandler.class),
            @Result(property = "ledgerInfo.ledgers", column = "ledger_info.ledgers", jdbcType = JdbcType.ARRAY, typeHandler = ArrayTypeHandler.class),
            @Result(property = "ledgerInfo.delLedgers", column = "ledger_info.del_ledgers", jdbcType = JdbcType.ARRAY, typeHandler = ArrayTypeHandler.class),
            @Result(property = "appInfo.jsonCategories", column = "app_info.categories"),
            @Result(property = "appInfo.jsonSubcategories", column = "app_info.subcategories"),
    })
    @SelectProvider(type = UserSqlProvider.class, method = "buildSqlGetUserByRowKey")
    UserModel getUserByUserId(@Param("regionId") Integer regionId, @Param("userId") Long userId, @Param("selectedFieldsName") List<String> selectedFieldsName);

    /**
     * Insert user.
     *
     * @param upsertUserDto user dto {@link UpsertUserDto}
     * @return the number of rows affected
     */
    @Insert("UPSERT INTO " + Constants.USER_TABLE + " (region_id, user_id, user_info.name, user_info.email, fund_info.funds, fund_info.del_funds, ledger_info.ledgers, ledger_info.del_ledgers, app_info.jsonCategories, app_info.jsonSubcategories) VALUES (#{regionId}, #{userId}, #{name}, #{email}, #{funds, jdbcType=ARRAY, typeHandler=org.apache.ibatis.type.ArrayTypeHandler}, #{delFunds, jdbcType=ARRAY, typeHandler=org.apache.ibatis.type.ArrayTypeHandler}, #{ledgers, jdbcType=ARRAY, typeHandler=org.apache.ibatis.type.ArrayTypeHandler}, #{delLedgers, jdbcType=ARRAY, typeHandler=org.apache.ibatis.type.ArrayTypeHandler}, #{categories}, #{subcategories})")
    int insertUser(UpsertUserDto upsertUserDto);

    /**
     * Update user.
     *
     * @param upsertUserDto user dto {@link UpsertUserDto}
     * @return the number of rows affected
     */
    @InsertProvider(type = UserSqlProvider.class, method = "buildSqlUpdateUser")
    int updateUser(UpsertUserDto upsertUserDto);
}
