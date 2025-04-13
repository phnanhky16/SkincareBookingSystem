package com.skincare_booking_system.dto.request;

import java.time.LocalDate;
import java.util.Set;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingRequest {
    long userId;
    long slotId;
    LocalDate bookingDate;
    Set<Long> serviceId;
    Long therapistId;
    Long voucherId;
}
