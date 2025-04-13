package com.skincare_booking_system.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TherapistRequest {
    @Size(min = 3, max = 20, message = "USERNAME_INVALID")
    String username;

    @Size(min = 8, max = 20, message = "PASSWORD_INVALID")
    String password;

    String fullName;

    @Email(message = "EMAIL_INVALID")
    String email;

    MultipartFile imgUrl;

    @Pattern(regexp = "^(84|0[35789])\\d{8}$", message = "Your phone number is not valid")
    String phone;

    String address;
    String gender;
    LocalDate birthDate;
    Integer yearExperience;
}
