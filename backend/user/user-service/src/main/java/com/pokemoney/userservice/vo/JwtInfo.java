package com.pokemoney.userservice.vo;

import lombok.*;

import java.util.Map;

/**
 * Info of jwt or redis stored value of jwt as key .
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtInfo {
    /**
     * id of jwt
     */
    private String id;
    /**
     * User's id.
     */
    private String userId;
    /**
     * User's email.
     */
    private String email;
    /**
     * User's username.
     */
    private String username;
    /**
     * User's role.
     */
    private String role;
    /**
     * User's permission.
     */
    private String permission;

    public JwtInfo fromMap(Map<String, String> map) {
        this.id = map.get("id");
        this.userId = map.get("userId");
        this.email = map.get("email");
        this.username = map.get("username");
        this.role = map.get("role");
        this.permission = map.get("permission");
        return this;
    }
}
