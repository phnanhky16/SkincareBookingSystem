package com.skincare_booking_system.validator;

import java.lang.annotation.*;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Documented
@Constraint(validatedBy = ImageURLValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ImageURL {

    String message() default "URL_INVALID";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
