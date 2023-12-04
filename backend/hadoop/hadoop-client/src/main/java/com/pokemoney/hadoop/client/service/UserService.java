package com.pokemoney.hadoop.client.service;

import com.pokemoney.hadoop.hbase.dto.user.UpsertUserDto;
import com.pokemoney.hadoop.hbase.phoenix.dao.UserMapper;
import com.pokemoney.hadoop.hbase.phoenix.model.UserModel;
import com.pokemoney.hadoop.hbase.utils.RowKeyUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * User table service
 */
@Slf4j
@Service
public class UserService {
    /**
     * user mapper
     */
    private final UserMapper userMapper;

    /**
     * Constructor
     *
     * @param userMapper user mapper
     */
    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    /**
     * Get user by user id
     *
     * @param userId user id
     * @return user model
     */
    public UserModel getUserByUserId(Long userId) {
        Integer regionId = RowKeyUtils.getRegionId(userId);
        return userMapper.getUserByUserId(regionId, userId, null);
    }

    /**
     * Insert user
     *
     * @param upsertUserDto upsert user dto
     * @return the number of rows affected
     */
    public int insertUser(UpsertUserDto upsertUserDto) {
        return userMapper.insertUser(upsertUserDto);
    }

    /**
     * Update user
     *
     * @param upsertUserDto upsert user dto
     * @return the number of rows affected
     */
    public int updateUser(UpsertUserDto upsertUserDto) {
        return userMapper.updateUser(upsertUserDto);
    }
}
