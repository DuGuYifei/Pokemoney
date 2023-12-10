package com.pokemoney.hadoop.hbase.phoenix.dao;

import com.pokemoney.hadoop.hbase.Constants;
import com.pokemoney.hadoop.hbase.dto.user.UpsertUserDto;
import com.pokemoney.hadoop.hbase.phoenix.dao.sqlprovider.UserSqlProvider;
import com.pokemoney.hadoop.hbase.phoenix.handler.StringListToArrayTypeHandler;
import com.pokemoney.hadoop.hbase.phoenix.model.UserModel;
import org.apache.ibatis.annotations.*;

import java.util.List;
import java.util.Map;

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
            @Result(property = "userInfo.name", column = "name"),
            @Result(property = "userInfo.email", column = "email"),
            @Result(property = "userInfo.updateUserInfoAt", column = "update_at"),
            @Result(property = "fundInfo.funds", column = "funds", typeHandler = StringListToArrayTypeHandler.class),
            @Result(property = "fundInfo.delFunds", column = "del_funds", typeHandler = StringListToArrayTypeHandler.class),
            @Result(property = "ledgerInfo.ledgers", column = "ledgers", typeHandler = StringListToArrayTypeHandler.class),
            @Result(property = "ledgerInfo.delLedgers", column = "del_ledgers", typeHandler = StringListToArrayTypeHandler.class),
            @Result(property = "appInfo.jsonCategories", column = "categories"),
            @Result(property = "appInfo.jsonSubcategories", column = "subcategories"),
            @Result(property = "notifications.notificationsJson", column = "new_notify"),
    })
    @SelectProvider(type = UserSqlProvider.class, method = "buildSqlGetUserByRowKey")
    UserModel getUserByUserId(@Param("regionId") Integer regionId, @Param("userId") Long userId, @Param("selectedFieldsName") List<String> selectedFieldsName);

    /**
     * Insert user.
     *
     * @param upsertUserDto user dto {@link UpsertUserDto}
     * @return the number of rows affected
     */
    @Insert("UPSERT INTO " + Constants.USER_TABLE + " (region_id, user_id, user_info.name, user_info.email, user_info.update_at, fund_info.funds, fund_info.del_funds, ledger_info.ledgers, ledger_info.del_ledgers, app_info.categories, app_info.subcategories) VALUES (#{regionId}, #{userId}, #{name}, #{email}, #{updateUserInfoAt}, #{fundInfo.funds, typeHandler = com.pokemoney.hadoop.hbase.phoenix.handler.StringListToArrayTypeHandler}, #{fundInfo.delFunds, typeHandler = com.pokemoney.hadoop.hbase.phoenix.handler.StringListToArrayTypeHandler}, #{ledgerInfo.ledgers, typeHandler = com.pokemoney.hadoop.hbase.phoenix.handler.StringListToArrayTypeHandler}, #{ledgerInfo.delLedgers, typeHandler = com.pokemoney.hadoop.hbase.phoenix.handler.StringListToArrayTypeHandler}, #{appInfo.jsonCategories}, #{appInfo.jsonSubcategories})")
    int insertUser(UpsertUserDto upsertUserDto);

    /**
     * Update user.
     *
     * @param upsertUserDto user dto {@link UpsertUserDto}
     * @return the number of rows affected
     */
    @UpdateProvider(type = UserSqlProvider.class, method = "buildSqlUpdateUser")
    int updateUser(UpsertUserDto upsertUserDto);
}
