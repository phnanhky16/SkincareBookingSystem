package com.skincare_booking_system.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class TherapistRevenueResponse {
    Long therapistId;
    String therapistName;
    int bookingQuantity;
    Double totalRevenue;
}
