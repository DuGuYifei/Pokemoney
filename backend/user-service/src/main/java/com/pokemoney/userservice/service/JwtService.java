package com.pokemoney.userservice.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.pokemoney.commons.http.errors.GenericForbiddenError;
import com.pokemoney.commons.http.errors.GenericInternalServerError;
import com.pokemoney.userservice.entity.UserEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Date;
import java.util.UUID;

/**
 * JSON Web Token service.
 * Must have a "jwt.secret" in properties file.
 */
@Service
public class JwtService {
    /**
     * JWT algorithm.
     */
    private final Algorithm algorithm;

    /**
     * Constructor.
     *
     * @param secret Secret.
     */
    public JwtService(@Value("${jwt.secret}") String secret) {
        algorithm = Algorithm.HMAC256(secret);
    }

    /**
     * Generates a JWT for the userEntity.
     *
     * @param userEntity UserEntity.
     * @return The JWT
     */
    public String generateJwt(UserEntity userEntity) {
        Date now = new Date();
        return JWT.create().withJWTId(UUID.randomUUID().toString()).withIssuedAt(now).withExpiresAt(new Date(now.getTime() + 2592000000L)) // 30 days
                .withSubject(userEntity.getId().toString()).withAudience(String.valueOf(userEntity.getServicePermission())).withClaim("role", userEntity.getUserRole().getRoleName()).sign(algorithm);
    }

    /**
     * Verifies a JWT and returns the user ID encoded in the token.
     * Check token by redis if redis alive. Otherwise, check token by JWT.
     * The audience should contain the permission
     * The expiration time will be accepted within 5 seconds.
     *
     * @param token The JWT
     * @param service The service name
     * @return The permission access code.
     */
    public Boolean verifyJwt(String token, String service) throws GenericForbiddenError, GenericInternalServerError {
        // Redis
        // TODO: Redis 应该在user service层做
        // TODO: 分解函数，verify token by jwt, decode token, permissionService compare permission

        // JWT
        String audience;
        try {
            audience = JWT.require(algorithm).acceptExpiresAt(5).build().verify(token).getAudience().get(0);
        } catch (Exception e) {
            throw new GenericForbiddenError("Invalid token");
        }
        BigInteger servicePermission = new BigInteger(audience);
        Integer serviceBit;
        try {
            serviceBit = PermissionService.getPermissionBitMap().get(service);
            if (serviceBit == null) {
                throw new GenericForbiddenError("Invalid service.");
            }
        } catch (Exception e) {
            throw new GenericInternalServerError("Failed to check service permission.");
        }
        if ((servicePermission.and(BigInteger.ONE.shiftLeft(serviceBit))).equals(BigInteger.ZERO)) {
            throw new GenericForbiddenError("No permission to access this service.");
        }
        return true;
    }
}
