package com.pokemoney.userservice.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.pokemoney.redis.service.api.*;
import com.pokemoney.redis.service.api.RedisTriService;
import com.pokemoney.redis.service.api.exceptions.RedisRpcException;
import com.pokemoney.userservice.Constants;
import com.pokemoney.userservice.Exceptions.JwtVerifyException;
import com.pokemoney.userservice.entity.UserEntity;
import com.pokemoney.userservice.vo.JwtInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.StatusRpcException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * JSON Web Token service.
 * Must have a "jwt.secret" in properties file.
 */
@Service
@Slf4j
public class JwtService {
    /**
     * JWT algorithm.
     */
    private final Algorithm algorithm;

    /**
     * Redis triple protocol service.
     */
    @DubboReference(version = "1.0.0", protocol = "tri", group = "redis", timeout = 10000)
    private final RedisTriService redisTriService;

    /**
     * Constructor.
     *
     * @param secret          Secret.
     * @param redisTriService     Redis client.
     */
    public JwtService(@Value("${jwt.secret}") String secret, RedisTriService redisTriService) {
        algorithm = Algorithm.HMAC256(secret);
        this.redisTriService = redisTriService;
    }

    /**
     * Generates a JWT for the userEntity.
     *
     * @param userEntity UserEntity.
     * @param jwtId      The JWT ID.
     * @return The JWT
     */
    public String generateJwt(UserEntity userEntity, String jwtId) {
        Date now = new Date();
        return JWT.create().withJWTId(jwtId).withIssuedAt(now).withExpiresAt(new Date(now.getTime() + 2592000000L)) // 30 days
                .withSubject(userEntity.getId().toString()).withAudience(String.valueOf(userEntity.getServicePermission())).withClaim("role", userEntity.getUserRole().getRoleName()).sign(algorithm);
    }

    /**
     * Store jwt status in redis.
     *
     * @param jwt        Json Web Token
     * @param userEntity UserEntity
     * @param jwtId      The id of jwt
     */
    public void storeJwtStatus(String jwt, UserEntity userEntity, String jwtId) {
        // The service should still work when there is no redis, this service can verify jwt. So use try catch.
        try {
            Map<String, String> map = new HashMap<>();
            map.put("id", jwtId);
            map.put("userId", userEntity.getId().toString());
            map.put("email", userEntity.getEmail());
            map.put("username", userEntity.getUsername());
            map.put("role", userEntity.getUserRole().getRoleName());
            map.put("permission", userEntity.getServicePermission().toString());
            redisTriService.hSet(RedisHashKeyValueDto.newBuilder()
                    .setKey(jwt)
                    .putAllValue(map)
                    .setTimeout(2592000L) // one month
                    .setPrefix(Constants.REDIS_TOKEN_PREFIX).build());
        } catch (Exception e) {
            log.error("Failed to send request to set jwt in redis.", e);
        }
    }

    /**
     * Verifies a JWT and returns the user ID encoded in the token.
     * Check token by redis if redis alive. Otherwise, check token by JWT.
     * The audience should contain the permission
     * The expiration time will be accepted within 5 seconds.
     *
     * @param token   The JWT
     * @return {@link JwtInfo} of the userEntity.
     */
    public JwtInfo verifyUserJwt(String token, String userId) throws JwtVerifyException.InvalidTokenException, JwtVerifyException.InvalidUserException {
        JwtInfo jwtInfo;
        try {
            jwtInfo = verifyUserJwtByRedis(token, userId);
            if (jwtInfo == null) {
                throw new JwtVerifyException.InvalidTokenException();
            }
        } catch (JwtVerifyException e) {
            throw e;
        } catch (Exception e) {
            log.warn("Failed to verify token by redis.", e);
            // If redis is not alive, check token by JWT algorithm.
            jwtInfo = verifyUserJwtByAlgorithm(token);
        }
        return jwtInfo;
    }

    /**
     * Verify token by redis.
     *
     * @param token token
     * @param userId userId
     * @return {@link JwtInfo} of the userEntity.
     * @throws JwtVerifyException.InvalidUserException InvalidUserException
     * @throws JwtVerifyException.InvalidTokenException InvalidTokenException
     */
    public JwtInfo verifyUserJwtByRedis(String token, String userId) throws JwtVerifyException.InvalidUserException, JwtVerifyException.InvalidTokenException {
        try {
            RedisResponseDto response = redisTriService.hGet(RedisHashKeyValueGetRequestDto.newBuilder().setKey(token).setKey(Constants.REDIS_TOKEN_PREFIX).build());
            RedisHashKeyValueDto redisHashKeyValueDto = response.getHashData();
            Map<String, String> hashMapInfo = redisHashKeyValueDto.getValueMap();
            JwtInfo jwtRedisInfo = new JwtInfo().fromMap(hashMapInfo);
            if (!jwtRedisInfo.getUserId().equals(userId.toString())) {
                throw new JwtVerifyException.InvalidUserException();
            }
            return jwtRedisInfo;
        } catch (RpcException e) {
            if (e.getCause() instanceof ExecutionException executionException) {
                if (executionException.getCause() instanceof StatusRpcException statusRpcException) {
                    if (RedisRpcException.KEY_NOT_FOUND.equals(statusRpcException.getStatus())) {
                        throw new JwtVerifyException.InvalidTokenException();
                    }
                }
            }
            throw new RuntimeException(e);
        }
    }

    /**
     * Verify token by JWT algorithm.
     *
     * @param token token
     * @return {@link JwtInfo} of the userEntity.
     * @throws JwtVerifyException.InvalidTokenException InvalidTokenException
     */
    public JwtInfo verifyUserJwtByAlgorithm(String token) throws JwtVerifyException.InvalidTokenException {
        DecodedJWT decodedJWT;
        try {
            decodedJWT = JWT.require(algorithm).acceptExpiresAt(5).build().verify(token);
        } catch (Exception e) {
            throw new JwtVerifyException.InvalidTokenException();
        }
        return JwtInfo.builder().id(decodedJWT.getId()).userId(decodedJWT.getSubject()).email(decodedJWT.getClaim("email").asString()).username(decodedJWT.getClaim("username").asString()).role(decodedJWT.getClaim("role").asString()).permission(decodedJWT.getAudience().get(0)).build();
    }
}
