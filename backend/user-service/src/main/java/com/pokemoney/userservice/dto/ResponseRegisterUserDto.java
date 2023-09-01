package com.pokemoney.userservice.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

/**
 * DTO for register user response.
 */
@Data
@Builder
public class ResponseRegisterUserDto {
    /**
     * The JWT for user.
     */
    @NonNull
    private String jwt;
}
