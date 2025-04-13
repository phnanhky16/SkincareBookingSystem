package com.skincare_booking_system.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePasswordRequest {

    String oldPassword;

    @NotBlank(message = "BLANK_FIELD")
    @Size(min = 8, message = "PASSWORD_INVALID")
    String newPassword;

    @NotBlank(message = "BLANK_FIELD")
    String confirmPassword;
}
