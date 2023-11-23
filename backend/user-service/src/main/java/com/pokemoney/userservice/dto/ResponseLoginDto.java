package com.pokemoney.userservice.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

/**
 * DTO for register user response.
 */
@Data
@Builder
public class ResponseLoginDto {
    /**
     * The user id.
     */
    @NonNull
    private Long id;

    /**
     * The user name.
     */
    @NonNull
    private String username;

    /**
     * The user email.
     */
    private String email;
}
