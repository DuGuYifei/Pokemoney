package com.pokemoney.commons.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.springframework.validation.FieldError;

import java.util.List;

/**
 * DTO for validation error result.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ResponseValidationErrorDto extends ResponseErrorDto{
    /**
     * The list of ValidationErrorDto.
     */
    @NonNull
    private final List<ValidationErrorDto> errors;

    /**
     * Constructor.
     *
     * @param message The error message.
     * @param status The HTTP status code of the response.
     */
    public ResponseValidationErrorDto(Integer status, String message, List<FieldError> errors) {
        super(status, message);
        this.errors = errors.stream().map(ValidationErrorDto::new).toList();
    }
}
