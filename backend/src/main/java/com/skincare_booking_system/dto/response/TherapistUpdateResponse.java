package com.skincare_booking_system.dto.response;

import java.time.LocalDate;

import jakarta.validation.constraints.Pattern;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TherapistUpdateResponse {
    String fullName;
    String email;

    @Pattern(regexp = "^(84|0[35789])\\d{8}$", message = "PHONENUMBER_INVALID")
    String phone;

    String address;
    String gender;
    LocalDate birthDate;
    Boolean status;
    Integer yearExperience;
}
