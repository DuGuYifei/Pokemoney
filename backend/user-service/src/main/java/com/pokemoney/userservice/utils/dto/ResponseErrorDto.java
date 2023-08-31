package com.pokemoney.userservice.utils.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

/**
 * DTO for error result.
 */
@Data
@Builder
@Validated
public class ResponseErrorDto {
    /**
     * The error message.
     */
    private String message;

    /**
     * The HTTP status code of the response.
     */
    private final boolean success = false;

    /**
     * The HTTP status code of the response.
     */
    @NotNull
    private int status;
}
