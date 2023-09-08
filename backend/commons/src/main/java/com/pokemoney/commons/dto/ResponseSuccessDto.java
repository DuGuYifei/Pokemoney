package com.pokemoney.commons.dto;

import lombok.*;

/**
 * DTO for success result.
 */
@Data
@Builder
public class ResponseSuccessDto<T> {
    /**
     * The HTTP status code of the response.
     */
    @Builder.Default
    private final Boolean success = true;

    /**
     * The HTTP status code of the response.
     */
    @NonNull
    private Integer status;

    /**
     * The error message.
     */
    private String message;

    /**
     * The data in the format of json.
     */
    private T data;
}
