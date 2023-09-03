package com.pokemoney.commons.dto;

import lombok.*;

/**
 * DTO for error result.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    @NonNull
    private Integer status;
}
