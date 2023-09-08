package com.pokemoney.commons.advice;

import com.pokemoney.commons.dto.ResponseDto;
import com.pokemoney.commons.errors.HttpBaseError;
import com.pokemoney.commons.dto.ResponseValidationErrorDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class GlobalRestControllerAdvice {
    /**
     * This method handles HttpBaseError exceptions and returns an HTTP response
     * with the appropriate status code and message.
     *
     * @param e The HttpBaseError object representing the exception
     * @return A ResponseEntity object with the status code and error message
     */
    @ExceptionHandler(HttpBaseError.class)
    public ResponseEntity<ResponseDto> httpErrorHandler(HttpBaseError e) {
        log.error("Handled HttpBaseError:------------------");
        log.error("", e);
        for (StackTraceElement element : e.getStackTrace()) {
            log.error("trace:{}", element);
        }
        log.error("----------------------------------------");
        ResponseDto result = ResponseDto
                .builder()
                .message(e.e.getMessage())
                .status(0)
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
        log.error("Handled BindException:", e);
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        ResponseValidationErrorDto result = new ResponseValidationErrorDto(
                "Validation failed",
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
    public ResponseEntity<ResponseDto> handleValidationException(ConstraintViolationException e) {
        log.error("Handle ConstraintViolationException:", e);
        Set<ConstraintViolation<?>> messages = e.getConstraintViolations();
        StringBuilder sb = new StringBuilder();
        for (ConstraintViolation<?> message : messages) {
            sb.append(message.getMessage());
            sb.append(";");
        }
        ResponseDto result = ResponseDto
                .builder()
                .message(sb.toString())
                .status(-1)
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
    public ResponseEntity<ResponseDto> handleUnknownException(Exception e) {
        log.error("Handle UnknownException:-------------------");
        log.error("", e);
        for (StackTraceElement element : e.getStackTrace()) {
            log.error("trace:{}", element);
        }
        log.error("------------------------------------------");
        e.printStackTrace();
        ResponseDto result = ResponseDto
                .builder()
                .message("Something went wrong")
                .status(0)
                .build();
        return ResponseEntity.status(422).body(result);
    }
}
