package com.pokemoney.userservice.utils.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

/**
 * DTO for success result.
 */
@Data
@Builder
public class ResponseSuccessDto {
    /**
     * The error message.
     */
    private String message;

    /**
     * The HTTP status code of the response.
     */
    private final boolean success = true;

    /**
     * The HTTP status code of the response.
     */
    @NotNull
    private int status;
}
