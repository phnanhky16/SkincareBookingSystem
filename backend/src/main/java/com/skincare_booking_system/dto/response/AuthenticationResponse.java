package com.skincare_booking_system.dto.response;

import com.skincare_booking_system.constant.Roles;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthenticationResponse {
    String token;
    Roles role;
    boolean success;
}
