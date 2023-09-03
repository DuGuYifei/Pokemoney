package com.pokemoney.commons.dto;

import lombok.*;

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
    @NonNull
    private Integer status;
}
