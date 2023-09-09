package com.pokemoney.userservice.entity;

import com.pokemoney.userservice.dto.RequestRegisterUserDto;
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
     * @return true if the password is correct
     */
    public Boolean verifyPassword(String password){
        System.out.println(password + " " + this.getPassword());
        return BCrypt.checkpw(password, this.getPassword());
    }
}
