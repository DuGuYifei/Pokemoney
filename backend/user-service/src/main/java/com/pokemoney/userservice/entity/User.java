package com.pokemoney.userservice.entity;

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

}
