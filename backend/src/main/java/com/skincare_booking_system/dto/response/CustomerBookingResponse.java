package com.skincare_booking_system.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import com.skincare_booking_system.constant.BookingStatus;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomerBookingResponse {
    long bookingId;
    String therapistName;
    LocalDate bookingDate;
    LocalTime bookingTime;
    Set<ServiceCusResponse> serviceName;

    @Enumerated(EnumType.STRING)
    BookingStatus status;
}
