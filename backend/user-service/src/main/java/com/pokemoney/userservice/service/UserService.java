package com.pokemoney.userservice.service;

import com.pokemoney.commons.http.errors.GenericForbiddenError;
import com.pokemoney.commons.http.errors.GenericInternalServerError;
import com.pokemoney.leaf.service.api.*;
import com.pokemoney.redis.service.api.*;
import com.pokemoney.redis.service.api.exceptions.RedisRpcException;
import com.pokemoney.userservice.Constants;
import com.pokemoney.userservice.dto.RequestLoginDto;
import com.pokemoney.userservice.dto.RequestRegisterCommonUserDto;
import com.pokemoney.userservice.dto.RequestVerifyLoginDto;
import com.pokemoney.userservice.entity.RoleEntity;
import com.pokemoney.userservice.entity.UserEntity;
import com.pokemoney.userservice.repository.UserRepository;
import com.pokemoney.userservice.utils.CodeGenerator;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.StatusRpcException;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigInteger;
import java.util.concurrent.ExecutionException;

/**
 * User business logic service.
 */
@Service
@Slf4j
@Validated
public class UserService {

    /**
     * Repository of t_users table.
     */
    private final UserRepository userRepository;

    /**
     * Leaf triple protocol service.
     */
    @DubboReference(version = "1.0.0", protocol = "tri", group = "leaf", timeout = 10000)
    private final LeafTriService leafTriService;

    /**
     * Redis triple protocol service.
     */
    @DubboReference(version = "1.0.0", protocol = "tri", group = "redis", timeout = 10000)
    private final RedisTriService redisTriService;

    /**
     * Constructor.
     *
     * @param userRepository        Repository of t_users table.
     */
    public UserService(UserRepository userRepository, LeafTriService leafTriService, RedisTriService redisTriService) {
        this.userRepository = userRepository;
        this.leafTriService = leafTriService;
        this.redisTriService = redisTriService;
    }

    /**
     * Save userEntity.
     *
     * @param userEntity UserEntity.
     * @return UserEntity saved.
     */
    public UserEntity save(UserEntity userEntity) {
        return userRepository.save(userEntity);
    }

    /**
     * Check if user exists by username.
     *
     * @param username Username.
     * @return True if user exists, false otherwise.
     */
    public boolean isExistsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    /**
     * Check if user exists by email.
     *
     * @param email Email.
     * @return True if user exists, false otherwise.
     */
    public boolean isExistsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    /**
     * Create a user from RegisterUserDto
     *
     * @param requestRegisterCommonUserDto RegisterUserDto contains user information from user.
     * @param roleEntity user role
     * @param permission the big integer of permission which each bit means one service
     * @return UserEntity created.
     */
    public UserEntity createUser(RequestRegisterCommonUserDto requestRegisterCommonUserDto, RoleEntity roleEntity, BigInteger permission) {
        LeafResponseDto leafResponseDto = leafTriService.getSnowflakeId(LeafGetRequestDto.newBuilder().setKey(Constants.USER_IN_LEAF_KEY).build());
        String snowflakeIdStr = leafResponseDto.getId();
        Long snowflakeId = Long.parseLong(snowflakeIdStr);
        UserEntity userEntity = UserEntity.fromRegisterUserDto(requestRegisterCommonUserDto, snowflakeId, roleEntity, permission);
        return save(userEntity);
    }

    /**
     * Find a user based on username or email.
     *
     * @param requestLoginDto The {@link RequestLoginDto} send by user.
     * @return The {@link RequestVerifyLoginDto} which will be verified.
     */
    public RequestVerifyLoginDto generateVerifyLoginDto(RequestLoginDto requestLoginDto) {
        RequestVerifyLoginDto requestVerifyLoginDto = RequestVerifyLoginDto.builder().password(requestLoginDto.getPassword()).build();
        if (requestLoginDto.getUsernameOrEmail().contains("@")) {
            requestVerifyLoginDto.setEmail(requestLoginDto.getUsernameOrEmail());
        } else {
            requestVerifyLoginDto.setUsername(requestLoginDto.getUsernameOrEmail());
        }
        return requestVerifyLoginDto;
    }

    /**
     * Verify login.
     * The user should exist.
     * The password should be correct.
     * The user should not be banned.
     *
     * @param requestVerifyLoginDto The {@link RequestVerifyLoginDto} to be verified.
     * @return UserEntity if login successfully, null otherwise.
     */
    public UserEntity verifyLogin(@Valid RequestVerifyLoginDto requestVerifyLoginDto) throws GenericForbiddenError {
        UserEntity userEntity;
        if (requestVerifyLoginDto.getEmail() != null) {
            userEntity = userRepository.findByEmail(requestVerifyLoginDto.getEmail());
        } else {
            userEntity = userRepository.findByUsername(requestVerifyLoginDto.getUsername());
        }
        if (userEntity == null) {
            throw new GenericForbiddenError("User not exist.");
        }
        if (!userEntity.verifyPassword(requestVerifyLoginDto.getPassword())) {
            throw new GenericForbiddenError("Invalid account or password.");
        }
        if (userEntity.getIsBan()) {
            throw new GenericForbiddenError("Sorry, your account is blocked.");
        }
        return userEntity;
    }

    /**
     * Try to register.
     *
     * @param requestRegisterCommonUserDto The {@link RequestRegisterCommonUserDto} to be verified.
     * @return The verification code.
     * @throws GenericInternalServerError If failed in internal server.
     * @throws GenericForbiddenError      If email or username already exists.
     */
    public String tryRegister(RequestRegisterCommonUserDto requestRegisterCommonUserDto) throws GenericInternalServerError, GenericForbiddenError {
        // TODO: read from slave node of db
        if (isExistsByEmail(requestRegisterCommonUserDto.getEmail())) {
            throw new GenericForbiddenError("Email already exists.");
        }
        if (isExistsByUsername(requestRegisterCommonUserDto.getUsername())) {
            throw new GenericForbiddenError("Username already exists.");
        }

        long verificationCode = CodeGenerator.GenerateNumber(Constants.VERIFICATION_CODE_LENGTH);
        String verificationCodeStr = String.format("%0" + Constants.VERIFICATION_CODE_LENGTH + "d", verificationCode);

        RedisKeyValueDto redisKeyValueDto = RedisKeyValueDto.newBuilder()
                .setKey(requestRegisterCommonUserDto.getEmail())
                .setValue(verificationCodeStr)
                .setTimeout(600L)
                .setPrefix(Constants.REDIS_REGISTER_PREFIX)
                .build();
        try {
            redisTriService.set(redisKeyValueDto);
        } catch (Exception e) {
            log.error("Failed to send request to set verification code in redis.", e);
            throw new GenericInternalServerError("Something wrong when generating verification code.");
        }
        return verificationCodeStr;
    }

    /**
     * Verify the registration code.
     *
     * @param requestRegisterCommonUserDto The {@link RequestRegisterCommonUserDto} to be verified.
     * @throws GenericForbiddenError If verification code not exist or expired,
     *                              or not correct,
     *                              or email or username already exists.
     */
    public void verifyRegister(RequestRegisterCommonUserDto requestRegisterCommonUserDto) throws GenericForbiddenError {
        // check username and email not exist
        // TODO: user read slave node of db
        if (isExistsByEmail(requestRegisterCommonUserDto.getEmail())) {
            throw new GenericForbiddenError("Email already exists.");
        }
        if (isExistsByUsername(requestRegisterCommonUserDto.getUsername())) {
            throw new GenericForbiddenError("Username already exists.");
        }

        RedisKeyValueGetRequestDto redisKeyValueGetRequestDto = RedisKeyValueGetRequestDto.newBuilder()
                .setKey(requestRegisterCommonUserDto.getEmail())
                .setPrefix(Constants.REDIS_REGISTER_PREFIX)
                .build();

        String verificationCode;
        RedisResponseDto responseFromRedis;

        // Get verification code from redis
        try {
            responseFromRedis = redisTriService.get(redisKeyValueGetRequestDto);
            verificationCode = responseFromRedis.getData().getValue();
            if (!verificationCode.equals(requestRegisterCommonUserDto.getVerificationCode())) {
                throw new GenericForbiddenError("Verification code not match.");
            }
        } catch (RpcException e) {
            if (e.getCause() instanceof ExecutionException executionException) {
                if (executionException.getCause() instanceof StatusRpcException statusRpcException) {
                    if (RedisRpcException.KEY_NOT_FOUND.equals(statusRpcException.getStatus())) {
                        throw new GenericForbiddenError("Verification code not exist or expired.");
                    }
                }
            }
            throw new RuntimeException("Failed to get verification code from redis.", e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to get verification code from redis.", e);
        }
    }
}
