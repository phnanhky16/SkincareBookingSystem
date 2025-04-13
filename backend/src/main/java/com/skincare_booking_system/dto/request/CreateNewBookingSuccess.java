package com.skincare_booking_system.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class CreateNewBookingSuccess {
    String to;
    String subject;
    LocalDate date;
    LocalTime time;
    String therapistName;
}
