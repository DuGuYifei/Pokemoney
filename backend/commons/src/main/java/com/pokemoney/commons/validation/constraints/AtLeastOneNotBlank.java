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
     *
     * @return the error message.
     */
    String message() default "{com.pokemoney.commons.validation.constraints.AtLeastOneNotBlank.message}";

    /**
     * The validation groups.
     *
     * @return the validation groups.
     */
    Class<?>[] groups() default {};

    /**
     * The payload.
     *
     * @return the payload.
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * The fields name to be checked.
     *
     * @return the fields to be checked.
     */
    String[] fields();
}
