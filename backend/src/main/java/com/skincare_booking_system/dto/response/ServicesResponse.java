package com.skincare_booking_system.dto.response;

import java.time.LocalTime;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ServicesResponse {
    Long serviceId;
    String serviceName;
    String description;
    String category;
    Double price;
    Boolean isActive;
    LocalTime duration;
    String imgUrl;
}
