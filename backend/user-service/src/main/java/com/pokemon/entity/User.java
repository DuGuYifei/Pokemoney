package com.pokemon.entity;

import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.AccessLevel;
import lombok.Setter;

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
    private String confirmation_key;

    /**
     * Whether Confirmation key is confirmed.
     */
    private Boolean is_confirm;

    /**
     * Whether user is banned.
     */
    private Boolean is_ban;

    /**
     * User register date.
     */
    private Timestamp register_date;

    /**
     * User last login date.
     */
    private Timestamp last_login_date;

}
