package com.pokemoney.userservice.entity;

import com.pokemoney.userservice.dto.RegisterUserDto;
import com.pokemoney.userservice.utils.enums.UserRole;
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

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "t_users")
public class User implements Serializable {

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
    private String username;

    /**
     * User password.
     */
    private String password;

    /**
     * User email.
     */
    private String email;

    /**
     * User confirmation key.
     */
    private String confirmationKey;

    /**
     * Whether Confirmation key is confirmed.
     */
    private Boolean isConfirm;

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
     * @param registerUserDto RegisterUserDto contains user information from user.
     * @return User object of user entity (NOT from persistent storage).
     */
    public static User fromRegisterUserDto(RegisterUserDto registerUserDto) {
        String hashedPassword = BCrypt.hashpw(registerUserDto.getPassword(), BCrypt.gensalt());
        return User.builder()
                .username(registerUserDto.getUsername())
                .password(registerUserDto.getPassword())
                .email(registerUserDto.getEmail())
                .build();
    }
}
