package com.pokemoney.userservice.entity;

import com.pokemoney.userservice.dto.RequestRegisterCommonUserDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.AccessLevel;
import lombok.Setter;
import org.springframework.security.crypto.bcrypt.BCrypt;

import java.io.Serial;
import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "t_users")
public class UserEntity implements Serializable {
    /**
     * Serial version UID.
     */
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * UserEntity id.
     */
    @Id
    private Long id;

    /**
     * UserEntity name.
     */
    @Column(unique = true)
    private String username;

    /**
     * UserEntity password.
     */
    private String password;

    /**
     * UserEntity email.
     */
    @Column(unique = true)
    private String email;

    /**
     * Whether user is banned.
     */
    private Boolean isBan;

    /**
     * UserEntity register date.
     */
    private Timestamp registerDate;

    /**
     * UserEntity last login date.
     */
    private Timestamp lastLoginDate;

    /**
     * UserEntity role.
     */
    @ManyToOne
    @JoinColumn(name = "role_id")
    private RoleEntity userRole;

    /**
     * UserEntity permission code, each binary bit represents a permission for one service.
     */
    private BigInteger servicePermission;

    /**
     * Create a user from RegisterUserDto
     *
     * @param requestRegisterCommonUserDto RegisterUserDto contains user information from user.
     * @param segmentId              Segment id.
     * @param roleEntity            RoleEntity object of user role.
     * @return UserEntity object of user entity (NOT from persistent storage).
     */
    public static UserEntity fromRegisterUserDto(RequestRegisterCommonUserDto requestRegisterCommonUserDto, Long segmentId, RoleEntity roleEntity, BigInteger permission) {
        String hashedPassword = BCrypt.hashpw(requestRegisterCommonUserDto.getPassword(), BCrypt.gensalt());
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        return UserEntity.builder()
                .id(segmentId)
                .username(requestRegisterCommonUserDto.getUsername())
                .password(hashedPassword)
                .email(requestRegisterCommonUserDto.getEmail())
                .isBan(false)
                .registerDate(timestamp)
                .lastLoginDate(timestamp)
                .userRole(roleEntity)
                .servicePermission(permission)
                .build();
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
     * @return true if the password is correct
     */
    public Boolean verifyPassword(String password){
        System.out.println(password + " " + this.getPassword());
        return BCrypt.checkpw(password, this.getPassword());
    }
}
