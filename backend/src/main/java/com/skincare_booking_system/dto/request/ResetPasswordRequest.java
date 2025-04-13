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
public class ResetPasswordRequest {
    @NotBlank(message = "BLANK_FIELD")
    @Size(min = 8, message = "PASSWORD_INVALID")
    String newPassword;

    @Size(min = 8, message = "PASSWORD_INVALID")
    String confirmPassword;
}
