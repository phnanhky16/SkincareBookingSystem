package com.skincare_booking_system.dto.response;

import java.time.LocalDate;
import java.util.Set;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SpecificTherapistScheduleResponse {
    long id;
    String therapistName;
    LocalDate workingDate;
    Set<Long> shiftId;
}
