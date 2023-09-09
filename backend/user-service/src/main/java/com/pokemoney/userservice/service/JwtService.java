package com.pokemoney.userservice.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.pokemoney.commons.http.errors.GenericForbiddenError;
import com.pokemoney.userservice.Constants;
import com.pokemoney.userservice.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

/**
 * JSON Web Token service. Must have a secret and aud in properties file.
 */
@Service
public class JwtService {
    /**
     * JWT audience.
     */
    @Value("${jwt.aud:general}")
    @NotBlank
    private String aud;

    /**
     * JWT algorithm.
     */
    private final Algorithm algorithm;

    /**
     * Constructor.
     */
    public JwtService(@Value("${jwt.secret}") String secret) {
        algorithm = Algorithm.HMAC256(secret);
    }

    /**
     * Generates a JWT for the user.
     *
     * @param user User.
     * @return The JWT
     */
    public String generateJwt(User user) {
        Date now = new Date();
        return JWT.create().withJWTId(UUID.randomUUID().toString()).withIssuedAt(now).withExpiresAt(new Date(now.getTime() + 2592000000L)) // 30 days
                .withSubject(user.getId().toString()).withAudience(aud).sign(algorithm);
    }

    /**
     * Verifies a JWT and returns the user ID encoded in the token.
     * The subject must be aud in this service.
     * The expiration time will be accepted within 5 seconds.
     *
     * @param token The JWT
     * @return The permission access code.
     */
    // TODO: modify the way for verifying jwt (the binary audience)
    public Long verifyJwtToken(String token) throws GenericForbiddenError {
        try {
            return Long.parseLong(JWT.require(algorithm).withAudience(aud).acceptExpiresAt(5).build().verify(token).getAudience().get(0));
        } catch (Exception e) {
            throw new GenericForbiddenError("Invalid token");
        }
    }
}
