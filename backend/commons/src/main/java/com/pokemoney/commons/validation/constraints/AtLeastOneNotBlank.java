package com.pokemoney.commons.validation.constraints;

import com.pokemoney.commons.validation.validator.AtLeastOneNotBlankValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;


/**
 * This annotation is used to validate that at least one of the fields is not blank.
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AtLeastOneNotBlankValidator.class)
@Documented
public @interface AtLeastOneNotBlank {
    /**
     * The error message.
     * @return the error message.
     */
    String message() default "{com.pokemoney.commons.validation.constraints.AtLeastOneNotBlank.message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] fields();
}
