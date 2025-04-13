package com.skincare_booking_system.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Pattern;

import com.skincare_booking_system.validator.GenderConstraint;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class StaffCreateCustomerRequest {
    String firstName;
    String lastName;

    @Pattern(regexp = "(84|0[35789])\\d{8}\\b", message = "INVALID_PHONE")
    String phone;

    @GenderConstraint(message = "GENDER_INVALID")
    String gender;

    LocalDate birthDate;
}
