package com.skincare_booking_system.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import com.skincare_booking_system.constant.BookingStatus;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingResponse {
    long id;
    long userId;
    long therapistId;
    LocalDate date;
    LocalTime time;

    Long voucherId;

    Set<Long> serviceId;

    BookingStatus status;
}
