package com.pokemoney.userservice.utils.enums;

import lombok.Getter;

/**
 * User role enum. It can be used for checking user role of permission, such as vip or permission for specific service.
 */
public enum UserRole {
    COMMON_USER(0, "COMMON USER");

    /**
     * User role integer value.
     */
    @Getter
    private final int value;

    /**
     * User role description.
     */
    @Getter
    private final String description;

    /**
     * Constructor of UserRole.
     * @param value Integer value of user role.
     * @param description Description of user role.
     */
    UserRole(int value, String description) {
        this.value = value;
        this.description = description;
    }
}
