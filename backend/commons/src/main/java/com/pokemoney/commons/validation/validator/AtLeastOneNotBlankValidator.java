package com.pokemoney.commons.validation.validator;

import com.pokemoney.commons.validation.constraints.AtLeastOneNotBlank;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.InvocationTargetException;

/**
 * This class implements the validation logic for AtLeastOneNotBlank annotation.
 */
public class AtLeastOneNotBlankValidator implements ConstraintValidator<AtLeastOneNotBlank, Object> {
    /**
     * The fields to be checked.
     */
    private String[] fields;

    /**
     * Initialize the validator in preparation for isValid calls.
     *
     * @param constraintAnnotation The annotation instance to apply.
     */
    @Override
    public void initialize(AtLeastOneNotBlank constraintAnnotation) {
        this.fields = constraintAnnotation.fields();
    }

    /**
     * Implements the validation logic.
     *
     * @param o                          The object to validate.
     * @param constraintValidatorContext context in which the constraint is evaluated.
     * @return true if valid, false otherwise.
     */
    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        for (String field : fields) {
            Object fieldValue = null;
            try {
                if (o.getClass().getDeclaredField(field).canAccess(o)) {
                    fieldValue = o.getClass().getDeclaredField(field).get(o);
                } else {
                    String getterMethodName = "get" + field.substring(0, 1).toUpperCase() + field.substring(1);
                    if (o.getClass().getDeclaredField(field).getType().equals(boolean.class)) {
                        getterMethodName = "is" + field.substring(0, 1).toUpperCase() + field.substring(1);
                    }
                    fieldValue = o.getClass().getDeclaredMethod(getterMethodName).invoke(o);
                }
            } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException | NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
            if (fieldValue != null && !fieldValue.toString().isBlank()) {
                return true;
            }
        }
        return false;
    }
}
