package com.pokemoney.userservice.entity;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.pokemoney.userservice.Constants;
import com.pokemoney.userservice.dto.RequestRegisterUserDto;
import com.pokemoney.userservice.errors.GenericForbiddenError;
import com.pokemoney.userservice.utils.enums.UserRole;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.AccessLevel;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.io.Serial;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "t_users")
public class User implements Serializable {

    private static final Algorithm algorithm = Algorithm.HMAC256(Constants.JWT_SECRET);

    /**
     * Serial version UID.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * User id.
     */
    @Id
    private Long id;

    /**
     * User name.
     */
    @Column(unique = true)
    private String username;

    /**
     * User password.
     */
    private String password;

    /**
     * User email.
     */
    @Column(unique = true)
    private String email;

    /**
     * Whether user is banned.
     */
    private Boolean isBan;

    /**
     * User register date.
     */
    private Timestamp registerDate;

    /**
     * User last login date.
     */
    private Timestamp lastLoginDate;

    /**
     * User role.
     */
    private UserRole userRole;

    /**
     * Create a user from RegisterUserDto
     *
     * @param requestRegisterUserDto RegisterUserDto contains user information from user.
     * @return User object of user entity (NOT from persistent storage).
     */
    public static User fromRegisterUserDto(RequestRegisterUserDto requestRegisterUserDto) {
        String hashedPassword = BCrypt.hashpw(requestRegisterUserDto.getPassword(), BCrypt.gensalt());
        return User.builder().id(requestRegisterUserDto.getId()).username(requestRegisterUserDto.getUsername()).password(hashedPassword).email(requestRegisterUserDto.getEmail()).isBan(false).registerDate(new Timestamp(System.currentTimeMillis())).userRole(UserRole.COMMON_USER).build();
    }

    /**
     * Ban user. In the future, it can be extended for
     * ban reason, ban by whom, ban date, etc.
     */
    public void ban() {
        this.setIsBan(true);
    }

    /**
     * Unban user. In the future, it can be extended for
     * set unban reason, unban by whom, unban date, etc. to NULL.
     */
    public void unban() {
        this.setIsBan(false);
    }

    /**
     * Verifies the password of the user.
     *
     * @param password the password to verify which is provided by user
     * @throws GenericForbiddenError if the password is incorrect
     */
    public void verifyPassword(String password) throws GenericForbiddenError {
        if (!BCrypt.checkpw(password, this.getPassword())) {
            throw new GenericForbiddenError("Invalid e-mail or password");
        }
    }

    /**
     * Generates a JWT for the user.
     *
     * @return The JWT
     */
    public String generateJwtToken() {
        Date now = new Date();
        return JWT.create().withJWTId(UUID.randomUUID().toString()).withIssuedAt(now).withExpiresAt(new Date(now.getTime() + 2592000000L)) // 30 days
                .withSubject(Constants.JWT_SUBJECT).withAudience(id.toString()).sign(algorithm);
    }

    /**
     * Verifies a JWT and returns the user ID encoded in the token.
     * The subject must be {@link Constants#JWT_SUBJECT}.
     * The expiration time will be accepted within 5 seconds.
     *
     * @param token The JWT
     * @return The user ID encoded in the token
     */
    public static Long verifyJwtToken(String token) throws GenericForbiddenError {
        try {
            return Long.parseLong(JWT.require(algorithm).withSubject(Constants.JWT_SUBJECT).acceptExpiresAt(5).build().verify(token).getAudience().get(0));
        } catch (Exception e) {
            throw new GenericForbiddenError("Invalid token");
        }
    }
}
