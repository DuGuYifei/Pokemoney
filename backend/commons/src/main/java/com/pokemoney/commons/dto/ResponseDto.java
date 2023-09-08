package com.pokemoney.commons.dto;

import lombok.*;

/**
 * The interface of Response DTO.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDto<T> {
    /**
     * The HTTP status code of the response.
     * <= 0: failure
     * > 0: success
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
