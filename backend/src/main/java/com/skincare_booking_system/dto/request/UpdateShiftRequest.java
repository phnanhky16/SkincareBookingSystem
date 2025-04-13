package com.skincare_booking_system.dto.request;

import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UpdateShiftRequest {
    LocalTime startTime;
    LocalTime endTime;
    Integer limitBooking;
}
