package com.pokemoney.commons.http.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.ConstraintViolation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import org.springframework.validation.FieldError;

import java.util.List;
import java.util.Set;

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

        public ValidationErrorDto(@NonNull String str, String message) {
            this.field = str;
            this.message = message;
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
     * @param errors  The list of validation FieldError.
     */
    public ResponseValidationErrorDto(String message, List<FieldError> errors) {
        super(-1, message, new ValidationErrorList(errors.stream().map(ValidationErrorDto::new).toList()));
    }

    /**
     * Constructor.
     *
     * @param message The error message.
     * @param errors  The set of validation ConstraintViolation.
     */
    public ResponseValidationErrorDto(String message, Set<? extends ConstraintViolation<?>> errors){
        super(-1, message, new ValidationErrorList(errors.stream()
                .map(
                        e -> {
                                String[] path = e.getPropertyPath().toString().split("\\.");
                                String field = path[path.length - 1];
                                field = field.replaceAll("([A-Z])", "_$1").toLowerCase();
                                return new ValidationErrorDto(field, e.getMessage());
                        }
                )
                .toList())
        );
    }
}
