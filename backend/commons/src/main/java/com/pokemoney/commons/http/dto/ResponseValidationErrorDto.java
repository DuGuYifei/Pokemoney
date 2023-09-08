package com.pokemoney.commons.http.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
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
public class ResponseValidationErrorDto extends ResponseDto<ResponseValidationErrorDto.ValidationErrorList>{
    /**
     * DTO for validation BindException or MethodArgumentNotValidException error result.
     */
    @Data
    public static class ValidationErrorDto {
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

    @Data
    @AllArgsConstructor
    public static class ValidationErrorList {
        @JsonProperty("error_list")
        List<ResponseValidationErrorDto.ValidationErrorDto> errorList;
    }

    /**
     * Constructor.
     *
     * @param message The error message.
     */
    public ResponseValidationErrorDto(String message, List<FieldError> errors) {
        super(-1, message, new ValidationErrorList(errors.stream().map(ValidationErrorDto::new).toList()));
    }
}
