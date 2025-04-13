package com.skincare_booking_system.dto.response;

import java.time.LocalTime;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ShiftResponse {
    long shiftId;
    LocalTime startTime;
    LocalTime endTime;
    int limitBooking;
}
