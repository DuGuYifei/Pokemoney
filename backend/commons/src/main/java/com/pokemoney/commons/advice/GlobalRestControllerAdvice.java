package com.pokemoney.commons.advice;

import com.pokemoney.commons.errors.HttpBaseError;
import com.pokemoney.commons.dto.ResponseErrorDto;
import com.pokemoney.commons.dto.ResponseValidationErrorDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Set;


/**
 * The global rest controller advice.
 */
@RestControllerAdvice
public class GlobalRestControllerAdvice {
    /**
     * This method handles HttpBaseError exceptions and returns an HTTP response
     * with the appropriate status code and message.
     *
     * @param e The HttpBaseError object representing the exception
     * @return A ResponseEntity object with the status code and error message
     */
    @ExceptionHandler(HttpBaseError.class)
    public ResponseEntity<ResponseErrorDto> httpErrorHandler(HttpBaseError e) {
        // TODO:Add log
        ResponseErrorDto result = ResponseErrorDto
                .builder()
                .message(e.e.getMessage())
                .status(e.statusCode)
                .build();
        return ResponseEntity.status(e.statusCode).body(result);
    }

    /**
     * This method handles validation exceptions and returns an HTTP response with a
     * 403 status code and the validation error message.
     *
     * @param e The BindException object representing the exception thrown when
     *          validation fails
     * @return A ResponseValidationErrorDto object with the 403 status code and
     *          the validation error message
     */
    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<ResponseValidationErrorDto> handleValidationException(BindException e) {
        // TODO: Add log
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        ResponseValidationErrorDto result = new ResponseValidationErrorDto(
                "Validation failed",
                400,
                fieldErrors);
        return ResponseEntity.status(400).body(result);
    }

    /**
     * This method handles validation exceptions and returns an HTTP response with a
     * 403 status code and the validation error message. The validation error occurs
     * when the validation is constraint like @NotBlank @NotNull @NotEmpty etc.
     *
     * @param e The ConstraintViolationException object representing the exception
     *          thrown when validation fails
     * @return A ResponseEntity object with the 403 status code and the validation error message
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseErrorDto> handleValidationException(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> messages = e.getConstraintViolations();
        StringBuilder sb = new StringBuilder();
        for (ConstraintViolation<?> message : messages) {
            sb.append(message.getMessage());
            sb.append(";");
        }
        ResponseErrorDto result = ResponseErrorDto
                .builder()
                .message(sb.toString())
                .status(400)
                .build();
        return ResponseEntity.status(400).body(result);
    }

    /**
     * This method handles unknown exceptions and returns an HTTP response with a
     * 422 status code and a generic error message.
     *
     * @param e The exception
     * @return A ResponseEntity object with the 422 status code and a generic error message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseErrorDto> handleUnknownException(Exception e) {
        // TODO: Add log
        e.printStackTrace();
        ResponseErrorDto result = ResponseErrorDto
                .builder()
                .message("Something went wrong")
                .status(422)
                .build();
        return ResponseEntity.status(422).body(result);
    }
}
