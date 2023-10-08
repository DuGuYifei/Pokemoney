package com.pokemoney.userservice.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.pokemoney.commons.http.dto.ResponseDto;
import com.pokemoney.commons.http.errors.GenericForbiddenError;
import com.pokemoney.commons.redis.RedisHashKeyValueDto;
import com.pokemoney.userservice.Constants;
import com.pokemoney.userservice.entity.UserEntity;
import com.pokemoney.userservice.feignclient.RedisClient;
import com.pokemoney.userservice.vo.JwtInfo;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
     * Redis client.
     */
    private final RedisClient redisClient;

    /**
     * Constructor.
     *
     * @param secret          Secret.
     * @param redisClient     Redis client.
     */
    public JwtService(@Value("${jwt.secret}") String secret, RedisClient redisClient) {
        algorithm = Algorithm.HMAC256(secret);
        this.redisClient = redisClient;
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
    // TODO: if redis is alive again, after check jwt by algorithm, store jwt status in redis. However, it should use thread pool not block user's request.
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
            redisClient.hSetKeyValue(RedisHashKeyValueDto.builder().key(jwt).value(map).timeout(2592000L) // one month
                    .prefix(Constants.REDIS_TOKEN_PREFIX).build());
        } catch (Exception e) {
            log.warn("Failed to send request to set jwt in redis.", e);
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
    public JwtInfo verifyUserJwt(String token, Long userId) throws GenericForbiddenError{
        JwtInfo jwtInfo;
        try {
            jwtInfo = verifyUserJwtByRedis(token, userId);
            if (jwtInfo == null) {
                throw new GenericForbiddenError("Invalid token");
            }
        } catch (GenericForbiddenError e) {
            throw e;
        } catch (Exception e) {
            log.warn("Failed to verify token by redis.", e);

            // If redis is not alive, check token by JWT algorithm.
            jwtInfo = verifyUserJwtByAlgorithm(token);
        }

        return jwtInfo;
    }

    public JwtInfo verifyUserJwtByRedis(String token, Long userId) throws GenericForbiddenError {
        try {
            ResponseDto<RedisHashKeyValueDto> responseDto = redisClient.hGetKeyValue(RedisHashKeyValueDto.builder().key(token).prefix(Constants.REDIS_TOKEN_PREFIX).build()).getBody();
            if (responseDto == null) {
                return null;
            }
            Map<String, String> hashMapInfo = responseDto.getData().getValue();
            JwtInfo jwtRedisInfo = new JwtInfo().fromMap(hashMapInfo);
            if (!jwtRedisInfo.getUserId().equals(userId.toString())) {
                throw new GenericForbiddenError("Invalid user for this token");
            }
            return jwtRedisInfo;
        } catch (FeignException e) {
            if (e.status() == 404) {
                throw new GenericForbiddenError("Invalid token");
            }
        }
        return null;
    }

    public JwtInfo verifyUserJwtByAlgorithm(String token) throws GenericForbiddenError {
        DecodedJWT decodedJWT;
        try {
            decodedJWT = JWT.require(algorithm).acceptExpiresAt(5).build().verify(token);
        } catch (Exception e) {
            throw new GenericForbiddenError("Invalid token");
        }
        return JwtInfo.builder().id(decodedJWT.getId()).userId(decodedJWT.getSubject()).email(decodedJWT.getClaim("email").asString()).username(decodedJWT.getClaim("username").asString()).role(decodedJWT.getClaim("role").asString()).permission(decodedJWT.getAudience().get(0)).build();
    }
}
