package com.skincare_booking_system.validator;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class GenderValidator implements ConstraintValidator<GenderConstraint, String> {

    private Set<String> validGenders;

    @Override
    public void initialize(GenderConstraint constraintAnnotation) {
        validGenders = new HashSet<>(Arrays.asList(constraintAnnotation.value()));
    }

    @Override
    public boolean isValid(String gender, ConstraintValidatorContext context) {
        if (gender == null) {
            return true;
        }
        return validGenders.contains(gender);
    }
}
