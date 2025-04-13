package com.skincare_booking_system.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import com.skincare_booking_system.validator.GenderConstraint;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserRegisterRequest {
    @Size(min = 3, max = 20, message = "USERNAME_INVALID")
    String username;

    @Size(min = 8, max = 20, message = "PASSWORD_INVALID")
    String password;

    @NotBlank(message = "BLANK_FIELD")
    String firstName;

    @NotBlank(message = "BLANK_FIELD")
    String lastName;

    @Email(message = "Your email is not corret")
    String email;

    @Pattern(regexp = "^(84|0[35789])\\d{8}$", message = "PHONENUMBER_INVALID")
    String phone;

    String address;

    @GenderConstraint(message = "GENDER_INVALID")
    String gender;

    LocalDate birthDate;
    Boolean status;
}
