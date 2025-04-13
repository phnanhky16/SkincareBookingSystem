package com.skincare_booking_system.validator;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ImageURLValidator implements ConstraintValidator<ImageURL, String> {

    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList("jpg", "jpeg", "png", "gif");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        try {
            URL url = new URL(value);
            String path = url.getPath().toLowerCase();
            String extension = path.substring(path.lastIndexOf(".") + 1);
            return ALLOWED_EXTENSIONS.contains(extension);
        } catch (Exception e) {
            return false;
        }
    }
}
