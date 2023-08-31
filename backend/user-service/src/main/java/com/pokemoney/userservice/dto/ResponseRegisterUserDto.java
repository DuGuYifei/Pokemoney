package com.pokemoney.userservice.dto;

import lombok.Builder;
import lombok.Data;

/**
 * DTO for register user response.
 */
@Data
@Builder
public class ResponseRegisterUserDto {
    /**
     * The JWT for user.
     */
    private String jwt;
}
