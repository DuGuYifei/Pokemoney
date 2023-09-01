package com.pokemoney.userservice.utils.dto;

import lombok.Data;
import lombok.NonNull;
import org.springframework.validation.FieldError;

/**
 * DTO for validation BindException or MethodArgumentNotValidException error result.
 */
@Data
public class ValidationErrorDto {
    /**
     * The field name.
     */
    @NonNull
    private final String field;

    /**
     * The error message.
     */
    private final String message;

    /**
     * Constructor.
     *
     * @param fieldError The FieldError object representing the exception
     */
    public ValidationErrorDto(FieldError fieldError) {
        this.field = fieldError.getField();
        this.message = fieldError.getDefaultMessage();
    }
}
