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
     * The HTTP status code of the response.
     */
    @Builder.Default
    private final Boolean success = false;

    /**
     * The HTTP status code of the response.
     */
    @NonNull
    private Integer status;

    /**
     * The error message.
     */
    private String message;
}
